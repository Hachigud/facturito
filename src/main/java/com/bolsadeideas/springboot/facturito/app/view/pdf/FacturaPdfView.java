package com.bolsadeideas.springboot.facturito.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.facturito.app.models.entity.Factura;
import com.bolsadeideas.springboot.facturito.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver localeResolver;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Factura factura = (Factura) model.get("factura");
		Locale locale = localeResolver.resolveLocale(request);
		
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);

		PdfPCell cell = null;
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente",null,locale)));
		cell.setBackgroundColor(new Color(170,210,250));
		cell.setPadding(8f);
		
		tabla.addCell(cell);	
		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);
		
		
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura",null,locale)));
		cell.setBackgroundColor(new Color(110,220,200));
		cell.setPadding(8f);
		
		tabla2.addCell(cell);
		tabla2.addCell(messageSource.getMessage("text.cliente.factura.folio",null,locale) + ": "+ factura.getId());
		tabla2.addCell(messageSource.getMessage("text.cliente.factura.descripcion",null,locale) + ": " + factura.getDescripcion());
		tabla2.addCell(messageSource.getMessage("text.cliente.factura.fecha",null,locale) + ": " + factura.getCreateAt());

		document.add(tabla);
		document.add(tabla2);
		
		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setSpacingAfter(20);
		tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
		tabla3.addCell(messageSource.getMessage("text.factura.form.item.nombre",null,locale) + ": ");
		tabla3.addCell(messageSource.getMessage("text.factura.form.item.precio",null,locale) + ": ");
		tabla3.addCell(messageSource.getMessage("text.factura.form.item.cantidad",null,locale) + ": ");
		tabla3.addCell(messageSource.getMessage("text.cliente.factura.total",null,locale));
		
		for(ItemFactura item: factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cell);
			tabla3.addCell(item.calcularImporte().toString());
		}
		
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.cliente.factura.total",null,locale)+":"));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(factura.getTotal().toString());
		document.add(tabla3);
		
		
		PdfPTable tabla4 = new PdfPTable(1);
		tabla4.setSpacingAfter(20);
		
		
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.form.observacion",null,locale)));
		cell.setBackgroundColor(new Color(170,150,180));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setPadding(8f);
		
		tabla4.addCell(cell);
		if(factura.getObservacion().isEmpty()) {
			tabla4.addCell(messageSource.getMessage("text.factura.ver.no.observaciones",null,locale));
		}else {
			
			tabla4.addCell(factura.getObservacion().toString());
		}
		
		document.add(tabla4);
	}

}
