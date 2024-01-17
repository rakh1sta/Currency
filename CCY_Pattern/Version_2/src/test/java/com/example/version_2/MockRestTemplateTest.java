package com.example.version_2;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.cbu.CbuCcyRecuestDto;
import com.example.version_2.dto.nbu.NbuCcyRecuestDto;
import com.example.version_2.entity.impls.CbuCcyEntity;
import com.example.version_2.entity.impls.NbuCcyEntity;
import com.example.version_2.mapper.impls.CbuCcyMapper;
import com.example.version_2.repositiry.impls.CbuCcyRepository;
import com.example.version_2.repositiry.impls.NbuCcyRepository;
import com.example.version_2.service.remoteServices.AbstractService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class MockRestTemplateTest {

    @Autowired
    private CbuCcyRepository cbuRepository;
    @Autowired
    private NbuCcyRepository nbuRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    MockRestServiceServer mockServer;

    @Autowired
    AbstractService<CbuCcyRepository, CbuCcyMapper> service;

    @Autowired
    RestTemplate restTemplate;

    private final String GET_CCY_LIST_REMOTE_API = "http://localhost:8080/rate-api/load-rates/%s";
    private final String CBU_API = "https://cbu.uz/ru/arkhiv-kursov-valyut/json/";
    private final String NBU_API = "https://nbu.uz/en/exchange-rates/json/";

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        cbuRepository.deleteAll();
        nbuRepository.deleteAll();
    }

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {"cbu", "cbu_ccy", "cbu_currency"})
    @DisplayName("test to get ccy list from CBU.uz with 200")
    void testGetCbuCcyListWith200(String remoteServiceName) throws Exception {
        List<CbuCcyRecuestDto> all = listOfCbuCcyObj();

        mockServer.expect(requestTo(CBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(listOfCbuCcyJson().toString(), MediaType.APPLICATION_JSON));

        String cbuRemoteUrl = GET_CCY_LIST_REMOTE_API.formatted(remoteServiceName);

        String cbuRequestResult = mockMvc.perform(get(cbuRemoteUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CcyResponseDto> dtoList = objectMapper.readValue(cbuRequestResult, new TypeReference<>() {
        });

        assertFalse(dtoList.isEmpty());
        assertEquals(all.size(), dtoList.size());
        assertNotEquals(dtoList.size() + 1, dtoList.size());

        CbuCcyRecuestDto dto = all.get(0);
        Optional<CbuCcyEntity> entity = cbuRepository.findByCurrency(dto.getCurrency());

        assertThat(entity).isNotEmpty();
        assertEquals(entity.get().getRate(), dto.getRate());
        assertEquals(entity.get().getDate(), dto.getDate());
        assertEquals(entity.get().getNameUzbek(), dto.getNameUzbek());
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"nbu", "nbu_ccy", "nbu_currency"})
    @DisplayName("test to get ccy list from NBU.uz with 200")
    void testGetNbuCcyListWith200(String remoteServiceName) throws Exception {
        List<NbuCcyRecuestDto> all = listOfNbuCcyObj();

        mockServer.expect(requestTo(NBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(listOfNbuCcyJson().toString(), MediaType.APPLICATION_JSON));

        String cbuRemoteUrl = GET_CCY_LIST_REMOTE_API.formatted(remoteServiceName);

        String nbuRequestResult = mockMvc.perform(get(cbuRemoteUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CcyResponseDto> dtoList = objectMapper.readValue(nbuRequestResult, new TypeReference<>() {
        });

        assertFalse(dtoList.isEmpty());
        assertEquals(all.size(), dtoList.size());
        assertNotEquals(dtoList.size() + 1, dtoList.size());

        NbuCcyRecuestDto dto = all.get(0);
        Optional<NbuCcyEntity> entity = nbuRepository.findByCurrency(dto.getCurrency());

        assertThat(entity).isNotEmpty();
        assertEquals(entity.get().getRate(), dto.getRate());
        assertEquals(entity.get().getDate(), dto.getDate());
        assertEquals(entity.get().getTitle(), dto.getTitle());
    }

    @Order(3)
    @Test
    @DisplayName("test to get empty ccy list from CBU.uz")
    void testGetEmptyCbuCcyList() {
        mockServer.expect(requestTo(CBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withNoContent());

        String response = service.getListDataFromRemote(CBU_API);

        assertTrue(response.contains("204"));
        assertTrue(response.contains("NO_CONTENT"));
        assertNotEquals("", response);
        assertNotEquals(null, response);
    }

    @Order(4)
    @Test
    @DisplayName("test to get empty ccy list from NBU.uz")
    void testGetEmptyNbuCcyList() {
        mockServer.expect(requestTo(NBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withNoContent());

        String response = service.getListDataFromRemote(NBU_API);

        assertTrue(response.contains("204"));
        assertTrue(response.contains("NO_CONTENT"));
        assertNotEquals("", response);
        assertNotEquals(null, response);
    }

    @Order(5)
    @Test
    @DisplayName("test to get ccr list from CBU.uz with Timeout")
    void testCbuCcyListReadTimeoutShouldFail() {
        mockServer.expect(requestTo(CBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withGatewayTimeout());

        String response = service.getListDataFromRemote(CBU_API);

        System.out.println(response);
        assertTrue(response.contains("504"));
        assertTrue(response.contains("Timeout"));
        assertTrue(response.contains("GATEWAY_TIMEOUT"));
        assertNotEquals("", response);
        assertNotEquals(null, response);
    }
    @Order(6)
    @Test
    @DisplayName("test to get ccr list from CBU.uz with Timeout")
    void testNbuCcyListReadTimeoutShouldFail() {
        mockServer.expect(requestTo(NBU_API))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withGatewayTimeout());

        String response = service.getListDataFromRemote(NBU_API);

        System.out.println(response);
        assertTrue(response.contains("504"));
        assertTrue(response.contains("Timeout"));
        assertTrue(response.contains("GATEWAY_TIMEOUT"));
        assertNotEquals("", response);
        assertNotEquals(null, response);
    }



    static List<String> listOfCbuCcyJson() {
        return List.of(
                "{\"id\":69,\"Code\":\"840\",\"Ccy\":\"USD\",\"CcyNm_RU\":\"\\u0414\\u043e\\u043b\\u043b\\u0430\\u0440 \\u0421\\u0428\\u0410\",\"CcyNm_UZ\":\"AQSH dollari\",\"CcyNm_UZC\":\"\\u0410\\u049a\\u0428 \\u0434\\u043e\\u043b\\u043b\\u0430\\u0440\\u0438\",\"CcyNm_EN\":\"US Dollar\",\"Nominal\":\"1\",\"Rate\":\"12281.14\",\"Diff\":\"11.03\",\"Date\":\"14.11.2023\"}",
                "{\"id\":21,\"Code\":\"978\",\"Ccy\":\"EUR\",\"CcyNm_RU\":\"\\u0415\\u0432\\u0440\\u043e\",\"CcyNm_UZ\":\"EVRO\",\"CcyNm_UZC\":\"E\\u0412\\u0420\\u041e\",\"CcyNm_EN\":\"Euro\",\"Nominal\":\"1\",\"Rate\":\"13127.31\",\"Diff\":\"26.51\",\"Date\":\"14.11.2023\"}",
                "{\"id\":57,\"Code\":\"643\",\"Ccy\":\"RUB\",\"CcyNm_RU\":\"\\u0420\\u043e\\u0441\\u0441\\u0438\\u0439\\u0441\\u043a\\u0438\\u0439 \\u0440\\u0443\\u0431\\u043b\\u044c\",\"CcyNm_UZ\":\"Rossiya rubli\",\"CcyNm_UZC\":\"\\u0420\\u043e\\u0441\\u0441\\u0438\\u044f \\u0440\\u0443\\u0431\\u043b\\u0438\",\"CcyNm_EN\":\"Russian Ruble\",\"Nominal\":\"1\",\"Rate\":\"133.41\",\"Diff\":\"0.08\",\"Date\":\"14.11.2023\"}"
        );
    }
    static List<String> listOfNbuCcyJson() {
        return List.of(
                "{\"title\": \"BAA dirhami\",\"code\": \"AED\",\"cb_price\": \"3340.62\",\"nbu_buy_price\": null,\"nbu_cell_price\": null,\"date\": \"13.11.2023 18:00:01\"}",
                "{\"title\": \"Avstraliya dollari\",\"code\": \"AUD\",\"cb_price\": \"7802.56\",\"nbu_buy_price\": null,\"nbu_cell_price\": null,\"date\": \"13.11.2023 18:00:01\"}",
                "{\"title\": \"Kanada dollari\",\"code\": \"CAD\",\"cb_price\": \"8886.88\",\"nbu_buy_price\": null,\"nbu_cell_price\": null,\"date\": \"13.11.2023 18:00:01\"}"
        );
    }

    List<CbuCcyRecuestDto> listOfCbuCcyObj() {
        try {
            List<CbuCcyRecuestDto> list = new ArrayList<>();
            for (String item : listOfCbuCcyJson()) {
                list.add(objectMapper.readValue(item, CbuCcyRecuestDto.class));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    List<NbuCcyRecuestDto> listOfNbuCcyObj() {
        try {
            List<NbuCcyRecuestDto> list = new ArrayList<>();
            for (String item : listOfNbuCcyJson()) {
                list.add(objectMapper.readValue(item, NbuCcyRecuestDto.class));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
