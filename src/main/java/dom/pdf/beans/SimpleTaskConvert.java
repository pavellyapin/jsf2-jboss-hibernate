package dom.pdf.beans;

//Java
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;

import com.myobjects.model.ScheduleItem;

/**
* This class demonstrates the conversion of an XML file to PDF using
* JAXP (XSLT) and FOP (XSL-FO).
*/
@Model
public class SimpleTaskConvert {

  /**
   * Main method.
   * @param args command-line arguments
   */
  public static void convert(ScheduleItem item , List<String> Friends) {
      try {
    	  FacesContext facesContext = FacesContext.getCurrentInstance();
    	  HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
    	  response.reset();   // Reset the response in the first place
          response.setHeader("Content-Type", "application/pdf");
          
          DOMSource src = XMLutils.SingleTaskXML(item, Friends);
          
          
          TransformerFactory DOMfactory = TransformerFactory.newInstance();
          Transformer DOMtransformer = DOMfactory.newTransformer();
          StreamResult result = new StreamResult();
          ByteArrayOutputStream DOMout = new ByteArrayOutputStream();
          result.setOutputStream(DOMout);
          DOMtransformer.transform(src, result);
          ByteArrayInputStream in = new ByteArrayInputStream(DOMout.toByteArray());
          
          OutputStream out2 = response.getOutputStream();

          try{
              CleanerProperties props = new CleanerProperties();
              props.setTranslateSpecialEntities(true);
              props.setTransResCharsToNCR(true);
              props.setOmitComments(true);
              TagNode tagNode = new HtmlCleaner(props).clean(in); 
              String newString=new PrettyHtmlSerializer(props).getAsString(tagNode, "ISO-8859-1");
              CYaHPConverter converter = new CYaHPConverter();
              //File fout = new File("C:\\RooApps\\aspose.pdf");
             // FileOutputStream out2 = new FileOutputStream(fout);
              Map properties = new HashMap();
              List headerFooterList = new ArrayList();
              properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);
              converter.convertToPdf(newString,IHtmlToPdfTransformer.A4L,headerFooterList, "file:///temp/",out2,properties);
              
          }catch(Exception e){
              e.printStackTrace();
          }
	       finally {
	    	   out2.flush();
	              out2.close();
	      }
          
      } catch (Exception e) {
          e.printStackTrace(System.err);
          System.exit(-1);
      }
  }
}