package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.ListMetadataFormatsParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;


public class ListMetadataFormatsParserTest extends AbstractParseTest {
    static String XML = "<ListMetadataFormats>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>uketd_dc</metadataPrefix>\r\n" +
            "            <schema>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/uketd_dc.xsd</schema>\r\n" +
            "            <metadataNamespace>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>dim</metadataPrefix>\r\n" +
            "            <schema>http://www.dspace.org/schema/dim.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.dspace.org/xmlns/dspace/dim</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>oai_dc</metadataPrefix>\r\n" +
            "            <schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.openarchives.org/OAI/2.0/oai_dc/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>marc</metadataPrefix>\r\n" +
            "            <schema>http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.loc.gov/MARC21/slim</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>etdms</metadataPrefix>\r\n" +
            "            <schema>http://www.ndltd.org/standards/metadata/etdms/1.0/etdms.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.ndltd.org/standards/metadata/etdms/1.0/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>xoai</metadataPrefix>\r\n" +
            "            <schema>http://www.lyncode.com/schemas/xoai.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.lyncode.com/xoai</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>qdc</metadataPrefix>\r\n" +
            "            <schema>http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd</schema>\r\n" +
            "            <metadataNamespace>http://purl.org/dc/terms/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>rdf</metadataPrefix>\r\n" +
            "            <schema>http://www.openarchives.org/OAI/2.0/rdf.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.openarchives.org/OAI/2.0/rdf/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>ore</metadataPrefix>\r\n" +
            "            <schema>http://tweety.lanl.gov/public/schemas/2008-06/atom-tron.sch</schema>\r\n" +
            "            <metadataNamespace>http://www.w3.org/2005/Atom</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>mods</metadataPrefix>\r\n" +
            "            <schema>http://www.loc.gov/standards/mods/v3/mods-3-1.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.loc.gov/mods/v3</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>mets</metadataPrefix>\r\n" +
            "            <schema>http://www.loc.gov/standards/mets/mets.xsd</schema>\r\n" +
            "            <metadataNamespace>http://www.loc.gov/METS/</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>didl</metadataPrefix>\r\n" +
            "            <schema>http://standards.iso.org/ittf/PubliclyAvailableStandards/MPEG-21_schema_files/did/didl.xsd</schema>\r\n" +
            "            <metadataNamespace>urn:mpeg:mpeg21:2002:02-DIDL-NS</metadataNamespace>\r\n" +
            "        </metadataFormat>\r\n" +
            "    </ListMetadataFormats>";

    @Test
    public void testParse() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        ListMetadataFormatsParser parser = new ListMetadataFormatsParser(theConfiguration());

        //System.out.println(parser.parse(reader));
        ListMetadataFormatsType result = parser.parse(reader);


        assertEquals(12, result.getMetadataFormat().size());
    }

}
