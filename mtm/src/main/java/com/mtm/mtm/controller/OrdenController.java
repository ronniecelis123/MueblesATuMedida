package com.mtm.mtm.controller;

import com.mtm.mtm.model.Orden;
import com.mtm.mtm.service.OrdenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mtm.mtm.service.PdfService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;

@Controller
public class OrdenController {

    @Autowired
    private OrdenService ordenService;
    @Autowired
    private PdfService pdfService;

    // ==========================
    // HISTORIAL
    // ==========================
    @GetMapping("/mis-ordenes")
    public String historial(Model model,
                            Authentication auth) {

        String email = auth.getName();

        List<Orden> ordenes =
                ordenService.obtenerCompras(email);

        model.addAttribute("ordenes", ordenes);

        return "mis-ordenes";
    }

    // ==========================
    // VER COMPROBANTE
    // ==========================
    @GetMapping("/orden/{id}")
    public String verOrden(@PathVariable Long id,
                           Model model) {

        Orden orden = ordenService.obtenerOrden(id);

        model.addAttribute("orden", orden);

        return "comprobante";
    }
    // ==========================
    // DESCARGAR PDF
    // ==========================
    @GetMapping("/orden/pdf/{id}")
    public ResponseEntity<InputStreamResource>
    descargarPdf(@PathVariable Long id) {

        Orden orden =
                ordenService.obtenerOrden(id);

        var pdf =
                pdfService.generarPdf(orden);

        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=comprobante.pdf"
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(
                        new InputStreamResource(pdf)
                );
    }
}