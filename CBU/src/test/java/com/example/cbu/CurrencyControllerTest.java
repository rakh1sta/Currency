package com.example.cbu;

import com.example.cbu.dto.CurrencyCreDto;
import com.example.cbu.dto.CurrencyDto;
import com.example.cbu.entity.Currency;
import com.example.cbu.mapper.CurrencyMapper;
import com.example.cbu.repositiry.CurrencyRepository;
import com.example.cbu.service.CurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class CurrencyControllerTest {

    @Autowired
    private CurrencyRepository repository;
    @Autowired
    @Mock
    private CurrencyService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    private final String CREATE_CCY_POST_REST_API = "/currency/";
    private final String FIND_CCY_CODE_GET_REST_API = "/currency/%s";

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        repository.deleteAll();
        createListOfCcy().forEach(currencyCreDto -> repository.save(mapper.toEntity(currencyCreDto)));
    }

    String createCurrencyJson() {
        return """
                {
                    "id": 123,
                    "Code": "345",
                    "Ccy": "ASD",
                    "CcyNm_RU": "Доллар США",
                    "CcyNm_UZ": "AQSH dollari",
                    "CcyNm_UZC": "АҚШ доллари",
                    "CcyNm_EN": "US Dollar",
                    "Nominal": "1",
                    "Rate": "12240.01",
                    "Diff": "-8.99",
                    "Date": "03.11.2023"
                }
                """;
    }

    CurrencyCreDto createCurrency() throws JsonProcessingException {
        return objectMapper.readValue(createCurrencyJson(), CurrencyCreDto.class);
    }


    @Test
    @Order(1)
    @DisplayName("check create new Ccy without exceptions")
    @Transactional
    void testCreateCcySuccessfully() throws Exception {
        CurrencyCreDto currencyCreDto = createCurrency();
        MvcResult mvcResult = mockMvc.perform(post(CREATE_CCY_POST_REST_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCurrencyJson()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(currencyCreDto.getCode())))
                .andExpect(jsonPath("$.currency", is(currencyCreDto.getCurrency())))
                .andExpect(jsonPath("$.date", is(currencyCreDto.getDate())))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CurrencyDto currencyDto = objectMapper.readValue(contentAsString, CurrencyDto.class);
        Map<String, Object> map = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertAll(() -> {
            assertThat(currencyDto).isNotNull();
            assertThat(currencyDto.getCode()).isNotNull();
            currencyDto.setCode(currencyDto.getCode() + "d");
            assertNotEquals(currencyDto.getCode(), currencyCreDto.getCode());
            assertAll(() -> {
                assertEquals(10, map.values().size());
                assertTrue(map.values().stream().anyMatch(Objects::nonNull));
            });
        });

    }


    @Test
    @Order(3)
    @Transactional
    @DisplayName("test to create exist currency with Already Exist exception ")
    void testAlreadyExistCCy() throws Exception {
        CurrencyCreDto currencyCreDto = createCurrency();
        Optional<Currency> beforeRequest = repository.findById(currencyCreDto.getId());
        assertThat(beforeRequest).isEmpty();
        //  post request for create  first create
        mockMvc.perform(post(CREATE_CCY_POST_REST_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCurrencyJson()))
                .andExpect(status().isCreated());

        Optional<Currency> afterRequest = repository.findById(currencyCreDto.getId());
        assertThat(afterRequest).isNotEmpty();

        //  post request for create  second create same ccy
        mockMvc.perform(post(CREATE_CCY_POST_REST_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCurrencyJson()))
                .andExpect(status().isAlreadyReported())
                .andExpect(jsonPath("$.message", is("Currency already exist")))
                .andExpect(jsonPath("$.request", is(CREATE_CCY_POST_REST_API)));
    }

    @Order(4)
    @DisplayName("test to find ccr by valid ccy code")
    @ParameterizedTest
    @MethodSource("createListOfCcy")
    void testFindCcyByValidCcyCode(CurrencyCreDto ccyObj) throws Exception {
        List<Currency> currencyList = repository.findAll();
        List<CurrencyCreDto> currencyCreDtoList = createListOfCcy();
        assertEquals(currencyList.size(), currencyCreDtoList.size());

        System.out.println(currencyList);
        String getApi = String.format(FIND_CCY_CODE_GET_REST_API, ccyObj.getCode());
        mockMvc.perform(get(getApi))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Currency> afterRequest = repository.findByCode(ccyObj.getCode());
        assertThat(afterRequest).isNotEmpty();

        Currency currency = mapper.toEntity(ccyObj);
        assertEquals(currency, afterRequest.get());
        assertEquals(currency.getCode(), afterRequest.get().getCode());

    }

    @Order(5)
    @ParameterizedTest
    @DisplayName("test to find ccr by in valid ccy code")
    @ValueSource(strings = {"2345", "763", "565"})
    void testFindCcyByIdValidCcyCode(String ccyCode) throws Exception {
        Optional<Currency> beforeRequest = repository.findByCode(ccyCode);
        assertThat(beforeRequest).isEmpty();
        String getApi = String.format(FIND_CCY_CODE_GET_REST_API, ccyCode);
        mockMvc.perform(get(getApi)
                        .content(createCurrencyJson()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Order(6)
    @Test
    @DisplayName("test to get ccr list from CBU.uz with 200")
    void testGetCcyListRemoteWith200() throws JsonProcessingException {
        List<Currency> all = repository.findAll();
        mockServer.expect(requestTo("https://cbu.uz/uz/arkhiv-kursov-valyut/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(all), MediaType.APPLICATION_JSON));
        String result = service.getListCurrencyRemote();
        List<Currency> dtos = objectMapper.readValue(result, new TypeReference<>() {});
        assertFalse(dtos.isEmpty());
        assertEquals(dtos.size(), all.size());
    }

    @Order(7)
    @Test
    @DisplayName("test to get empty ccr list from CBU.uz")
    void testGetEmptyCcyListRemote()  {
        mockServer.expect(requestTo("https://cbu.uz/uz/arkhiv-kursov-valyut/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withNoContent());
        String result = service.getListCurrencyRemote();
        System.out.println(result);
        assertTrue(result.contains("No Content"));
    }

    @Order(8)
    @Test
    @DisplayName("test to get ccr list from CBU.uz with Timeout")
    void testGetCcyListReturnReadTimeoutShouldFail() {
        mockServer.expect(requestTo("https://cbu.uz/uz/arkhiv-kursov-valyut/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withGatewayTimeout());
        String result = service.getListCurrencyRemote();
        System.out.println(result);
        assertTrue(result.contains("Timeout"));
        assertTrue(result.contains("504"));

    }

    private static List<CurrencyCreDto> createListOfCcy() {
        return List.of(
                CurrencyCreDto.builder().id(69).code("840").currency("USD").nameRussian("Доллар США").nameUzbek("AQSH dollari").nameUzbekKrill("АҚШ доллари").nameEnglish("US Dollar").nominal("1").rate("23").difference("3").date("132").build(),
                CurrencyCreDto.builder().id(21).code("234").currency("EUR").nameRussian("Доллар США").nameUzbek("AQSH dollari").nameUzbekKrill("АҚШ доллари").nameEnglish("US Dollar").nominal("1").rate("34").difference("43").date("234").build(),
                CurrencyCreDto.builder().id(57).code("123").currency("RUB").nameRussian("Доллар США").nameUzbek("AQSH dollari").nameUzbekKrill("АҚШ доллари").nameEnglish("US Dollar").nominal("1").rate("32").difference("234").date("345").build()
        );
    }


}
