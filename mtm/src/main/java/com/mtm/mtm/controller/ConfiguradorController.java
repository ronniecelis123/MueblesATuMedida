package com.mtm.mtm.controller;

import com.mtm.mtm.model.*;
import com.mtm.mtm.repository.*;
import com.mtm.mtm.service.ConfiguracionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
public class ConfiguradorController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private AcabadoRepository acabadoRepository;

    @Autowired
    private TipoPataRepository tipoPataRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==========================
    // MÉTODO PARA OBTENER USUARIO
    // ==========================
    private Usuario obtenerUsuario(Authentication authentication) {

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            return usuarioRepository.findByEmail(authentication.getName());
        }

        return null;
    }

    // ==========================
    // CATÁLOGO
    // ==========================
    @GetMapping("/")
    public String catalogo(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoria,
            Model model,
            Authentication authentication
    ) {

        Usuario usuario = obtenerUsuario(authentication);
        model.addAttribute("usuarioSesion", usuario);

        List<Producto> productos;

        if (search != null && !search.isEmpty()) {
            productos = productoRepository.findByActivoTrueAndNombreContainingIgnoreCase(search);
        } else if (categoria != null && !categoria.isEmpty()) {
            productos = productoRepository.findByActivoTrueAndCategoria(categoria);
        } else {
            productos = productoRepository.findByActivoTrue();
        }

        model.addAttribute("productos", productos);

        return "catalogo";
    }

    // ==========================
    // RECARGAR CONFIGURADOR
    // ==========================
    private String recargarConfigurador(Integer productoId, Model model, Usuario usuario) {

        Producto producto = productoRepository.findById(productoId).orElse(null);

        model.addAttribute("usuarioSesion", usuario);
        model.addAttribute("producto", producto);
        model.addAttribute("materiales", materialRepository.findAll());
        model.addAttribute("acabados", acabadoRepository.findAll());
        model.addAttribute("patas", tipoPataRepository.findAll());

        return "configurador";
    }

    // ==========================
    // CALCULAR PRECIO
    // ==========================
    @PostMapping("/calcular")
    public String calcularPrecio(
            @RequestParam Integer productoId,
            @RequestParam Integer materialId,
            @RequestParam Integer acabadoId,
            @RequestParam Integer pataId,
            @RequestParam BigDecimal largo,
            @RequestParam BigDecimal ancho,
            @RequestParam Double alto,
            Model model,
            Authentication authentication
    ) {

        Usuario usuario = obtenerUsuario(authentication);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioSesion", usuario);

        // VALIDACIONES
        if (largo.compareTo(BigDecimal.ZERO) <= 0 ||
                ancho.compareTo(BigDecimal.ZERO) <= 0) {

            model.addAttribute("error", "Las dimensiones deben ser mayores a 0");
            return recargarConfigurador(productoId, model, usuario);
        }

        if (largo.compareTo(new BigDecimal("500")) > 0 ||
                ancho.compareTo(new BigDecimal("500")) > 0) {

            model.addAttribute("error", "Dimensiones demasiado grandes");
            return recargarConfigurador(productoId, model, usuario);
        }

        BigDecimal largoM = largo.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal anchoM = ancho.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        Producto producto = productoRepository.findById(productoId).orElseThrow();
        Material material = materialRepository.findById(materialId).orElseThrow();
        Acabado acabado = acabadoRepository.findById(acabadoId).orElseThrow();
        TipoPata pata = tipoPataRepository.findById(pataId).orElseThrow();

        BigDecimal precio = configuracionService.calcularPrecio(
                producto, material, acabado, pata, largoM, anchoM
        );

        precio = precio.setScale(2, RoundingMode.HALF_UP);

        // GUARDAR CONFIGURACIÓN
        Configuracion config = new Configuracion();
        config.setProducto(producto);
        config.setMaterial(material);
        config.setAcabado(acabado);
        config.setTipoPata(pata);
        config.setLargo(largo.doubleValue());
        config.setAncho(ancho.doubleValue());
        config.setAlto(alto);
        config.setPrecioCalculado(precio.doubleValue());

        config.setUsuario(usuario);

        Configuracion configGuardada = configuracionRepository.save(config);

        // ENVIAR A VISTA
        model.addAttribute("configuracion", configGuardada);
        model.addAttribute("precio", precio);
        model.addAttribute(
                "materialSeleccionado",
                materialId
        );

        model.addAttribute(
                "acabadoSeleccionado",
                acabadoId
        );

        model.addAttribute(
                "pataSeleccionada",
                pataId
        );

        model.addAttribute(
                "largoSeleccionado",
                largo
        );

        model.addAttribute(
                "anchoSeleccionado",
                ancho
        );

        model.addAttribute(
                "altoSeleccionado",
                alto
        );

        return recargarConfigurador(productoId, model, usuario);
    }

    // ==========================
    // CONFIGURAR PRODUCTO
    // ==========================
    @GetMapping("/configurar/{id}")

    public String configurar(@PathVariable Integer id, Model model, HttpServletRequest request) {

        Usuario usuario = null;

        if (request.getUserPrincipal() != null) {

            String email = request.getUserPrincipal().getName();

            usuario = usuarioRepository.findByEmail(email);
        }

        model.addAttribute("usuarioSesion", usuario);
        model.addAttribute("usuarioSesion", usuario);

        Producto producto = productoRepository.findById(id).orElseThrow();

        model.addAttribute("producto", producto);
        model.addAttribute("materiales", materialRepository.findAll());
        model.addAttribute("acabados", acabadoRepository.findAll());
        model.addAttribute("patas", tipoPataRepository.findAll());

        return "configurador";
    }
}