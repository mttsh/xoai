package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.MapBuilder;
import com.lyncode.xoai.builders.OAIRequestParametersBuilder;
import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.xoai.tests.SyntacticSugar.and;
import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static com.lyncode.xoai.tests.SyntacticSugar.with;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class ListSetsVerbTest extends AbstractDataProviderTest {

    private static final String NAME_1 = "NAME_1";
    private static final String SPEC_1 = "SPEC_1";
    private static final String NAME_2 = "NAME_2";
    private static final String SPEC_2 = "SPEC_2";

    @Test
    public void shouldListGivenSetList () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theSetRepository())
                .withRandomSets(2);

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), hasXPath("count(//o:set)", String.valueOf(2)));
    }

    @Test
    public void shouldReturnResumptionTokenIfExceedsTheMaximumSetsPerPage () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException, DocumentException {
        given(theConfiguration().withMaxListSets(5));
        and(given(theSetRepository()))
            .withRandomSets(9);

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), hasXPath("count(//o:set)", String.valueOf(5)));
        assertThat(theResult(), hasXPath("//o:resumptionToken"));

        String resumptionToken = getXPath("//o:resumptionToken");

        afterHandling(aRequest().withVerb("ListSets").withResumptionToken(resumptionToken));

        assertThat(theResult(), hasXPath("count(//o:set)", String.valueOf(4)));
        assertThat(theResult(), hasXPath("//o:resumptionToken", ""));
    }

    @Test
    public void shouldReturnAnErrorIfItDoesNotSupportSets () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theSetRepository().doesntSupportSets());

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), hasXPath("count(//o:error)", String.valueOf(1)));
        assertThat(theResult(), hasXPath("//o:error/@code", "noSetHierarchy"));
    }

}
