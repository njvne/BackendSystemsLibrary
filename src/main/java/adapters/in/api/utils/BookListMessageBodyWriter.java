package adapters.in.api.utils;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

@Provider
public class BookListMessageBodyWriter implements MessageBodyWriter<List<?>>
{

	//unsure about necessity

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return List.class.isAssignableFrom(type) && mediaType.isCompatible( MediaType.APPLICATION_XML_TYPE);
	}

	@Override
	public void writeTo(List<?> list, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
						MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
	{
		try (OutputStreamWriter writer = new OutputStreamWriter(entityStream))
		{
			String s = type == null ? "" : type.toString();
			writer.write("<books>\n");
			for (Object bookitem : list)
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(bookitem.getClass());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshaller.marshal(bookitem, writer);
			}
			writer.write("</books>\n");
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error serializing list to XML", e);
		}
	}
}