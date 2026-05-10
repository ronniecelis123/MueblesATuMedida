package com.mtm.mtm.controller;

import com.mtm.mtm.model.Configuracion;
import com.mtm.mtm.model.Orden;
import com.mtm.mtm.model.OrdenDetalle;
import com.mtm.mtm.repository.ConfiguracionRepository;
import com.mtm.mtm.repository.OrdenDetalleRepository;
import com.mtm.mtm.repository.OrdenRepository;
import com.mtm.mtm.service.OrdenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private OrdenDetalleRepository ordenDetalleRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    // ==========================
    // VER CARRITO
    // ==========================
    @GetMapping
    public String verCarrito(Model model, Authentication auth) {

        String email = auth.getName();

        Orden carrito = ordenService.obtenerCarritoActivo(email);

        model.addAttribute("carrito", carrito);

        return "carrito";
    }

    // ==========================
    // AGREGAR AL CARRITO
    // ==========================
    @PostMapping("/agregar")
    public String agregar(@RequestParam Integer configuracionId,
                          Authentication auth) {

        String email = auth.getName();

        Orden carrito = ordenService.obtenerCarritoActivo(email);

        Configuracion config = configuracionRepository
                .findById(configuracionId).orElseThrow();

        OrdenDetalle detalle = new OrdenDetalle();
        detalle.setOrden(carrito);
        detalle.setConfiguracion(config);
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(config.getPrecioCalculado());
        detalle.setSubtotal(config.getPrecioCalculado());

        ordenDetalleRepository.save(detalle);

        Double total = ordenDetalleRepository.calcularTotal(carrito.getIdOrden());
        System.out.println("TOTAL CALCULADO: " + total);
        carrito.setTotal(total);

        ordenRepository.save(carrito);

        return "redirect:/carrito";
    }
    @Transactional
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer detalleId,
                           Authentication auth) {

        System.out.println("ENTRO A ELIMINAR");
        System.out.println("ID: " + detalleId);

        String email = auth.getName();

        Orden carrito = ordenService.obtenerCarritoActivo(email);

        OrdenDetalle detalle = ordenDetalleRepository
                .findById(detalleId).orElseThrow();

        if (!detalle.getOrden().getIdOrden().equals(carrito.getIdOrden())) {
            return "redirect:/";
        }

        carrito.getDetalles().remove(detalle);

        detalle.setOrden(null);

        ordenDetalleRepository.delete(detalle);

        Double total = ordenDetalleRepository.calcularTotal(carrito.getIdOrden());

        carrito.setTotal(total);

        ordenRepository.save(carrito);

        return "redirect:/carrito";
    }
    // ==========================
    // CHECKOUT
    // ==========================
    @PostMapping("/checkout")
    public String checkout(Authentication auth,
                           Model model) {

        String email = auth.getName();

        Orden orden = ordenService.finalizarCompra(email);

        if (orden == null) {
            return "redirect:/carrito?vacio";
        }

        model.addAttribute("orden", orden);

        return "comprobante";
    }
}