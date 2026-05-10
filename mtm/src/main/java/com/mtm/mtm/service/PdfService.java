package com.mtm.mtm.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.mtm.mtm.model.Orden;
import com.mtm.mtm.model.OrdenDetalle;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public ByteArrayInputStream generarPdf(Orden orden) {

        Document document = new Document();

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            // ==========================
            // TITULO
            // ==========================
            Font titulo =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            22
                    );

            Paragraph p =
                    new Paragraph("Muebles a tu medida", titulo);

            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Comprobante de compra"));
            document.add(new Paragraph(" "));



            document.add(new Paragraph(
                    "Cliente: " +
                            orden.getUsuario().getNombre()
            ));

            document.add(new Paragraph(
                    "Email: " +
                            orden.getUsuario().getEmail()
            ));

            document.add(new Paragraph(" "));

            // ==========================
            // TABLA
            // ==========================
            PdfPTable table =
                    new PdfPTable(4);

            table.setWidthPercentage(100);

            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio");
            table.addCell("Subtotal");

            for (OrdenDetalle d : orden.getDetalles()) {

                table.addCell(
                        d.getConfiguracion()
                                .getProducto()
                                .getNombre()
                );

                table.addCell(
                        String.valueOf(
                                d.getCantidad()
                        )
                );

                table.addCell(
                        "$" + d.getPrecioUnitario()
                );

                table.addCell(
                        "$" + d.getSubtotal()
                );
            }

            document.add(table);

            document.add(new Paragraph(" "));

            // ==========================
            // TOTAL
            // ==========================
            Font totalFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            18
                    );

            Paragraph total =
                    new Paragraph(
                            "TOTAL: $" + orden.getTotal(),
                            totalFont
                    );

            total.setAlignment(Element.ALIGN_RIGHT);

            document.add(total);

            document.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return new ByteArrayInputStream(
                out.toByteArray()
        );
    }
}