package com.example.version_2;


import com.example.version_2.dto.CcyResponseDto;
import com.example.version_2.dto.cbu.CbuCcyRecuestDto;
import com.example.version_2.dto.nbu.NbuCcyRecuestDto;
import com.example.version_2.entity.impls.CbuCcyEntity;
import com.example.version_2.entity.impls.NbuCcyEntity;
import com.example.version_2.mapper.impls.CbuCcyMapper;
import com.example.version_2.mapper.impls.NbuCcyMapper;
import com.example.version_2.repositiry.impls.CbuCcyRepository;
import com.example.version_2.repositiry.impls.NbuCcyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class RemoteControllerTest {

    @Autowired
    private CbuCcyRepository cbuRepository;
    @Autowired
    private NbuCcyRepository nbuRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CbuCcyMapper cbuCcyMapper;
    @Autowired
    private NbuCcyMapper nbuCcyMapper;

    @Autowired
    private MockMvc mockMvc;

    private final String GET_CCY_LIST_REMOTE_API = "http://localhost:8080/rate-api/load-rates/%s";
    private final String GET_AVR_CCY_APIS = "http://localhost:8080/rate-api/average/%s";
    private final String GET_AVR_CCY_API = "http://localhost:8080/rate-api/%s/%s";

    @BeforeEach
    void init() {
        cbuRepository.deleteAll();
        nbuRepository.deleteAll();
    }

    @Order(1)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"cbu", "cbu_ccy", "cbu_currency"})
    @DisplayName("load cbu currencies with 200")
    void testLoadCbuCcyListWith200(String cbuServiceName) throws Exception {
        String cbuRemoteUrl = GET_CCY_LIST_REMOTE_API.formatted(cbuServiceName);

        String cbuRequestResult = mockMvc.perform(get(cbuRemoteUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CcyResponseDto> cbuResponseDtoList = objectMapper.readValue(cbuRequestResult, new TypeReference<>() {
        });
        List<CbuCcyEntity> cbyCcyList = cbuRepository.findAll();

        int webResponseListSize = cbuResponseDtoList.size();
        int repositoryResponseListSize = cbyCcyList.size();

        CcyResponseDto firstElement1 = cbuResponseDtoList.get(0);
        CbuCcyEntity firstElement2 = cbyCcyList.get(0);

        assertThatList(cbuResponseDtoList).isNotEmpty();
        assertThatList(cbyCcyList).isNotEmpty();

        assertNotEquals(webResponseListSize + 1, webResponseListSize);
        assertNotEquals(webResponseListSize + 1, repositoryResponseListSize);
        assertEquals(webResponseListSize, repositoryResponseListSize);
        assertNotEquals(firstElement1, firstElement2);
        assertEquals(firstElement1.getRate(), firstElement2.getRate());
        assertEquals(firstElement1.getCurrency(), firstElement2.getCurrency());

        firstElement1.setRate(firstElement1.getRate().multiply(BigDecimal.TEN));
        assertNotEquals(firstElement1.getRate(), firstElement2.getRate());
    }

    @Order(2)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"nbu", "nbu_ccy", "nbu_currency"})
    @DisplayName("load nbu currencies with 200")
    void testLoadNbuCcyListWith200(String nbuServiceName) throws Exception {
        String nbuRemoteUrl = GET_CCY_LIST_REMOTE_API.formatted(nbuServiceName);

        String nbuRequestResult = mockMvc.perform(get(nbuRemoteUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CcyResponseDto> cbuResponseDtoList = objectMapper.readValue(nbuRequestResult, new TypeReference<>() {
        });
        List<NbuCcyEntity> cbyCcyList = nbuRepository.findAll();

        int webResponseListSize = cbuResponseDtoList.size();
        int repositoryResponseListSize = cbyCcyList.size();

        CcyResponseDto firstElement1 = cbuResponseDtoList.get(0);
        NbuCcyEntity firstElement2 = cbyCcyList.get(0);

        assertThatList(cbuResponseDtoList).isNotEmpty();
        assertThatList(cbyCcyList).isNotEmpty();

        assertNotEquals(webResponseListSize + 1, webResponseListSize);
        assertNotEquals(webResponseListSize + 1, repositoryResponseListSize);
        assertEquals(webResponseListSize, repositoryResponseListSize);
        assertNotEquals(firstElement1, firstElement2);
        assertEquals(firstElement1.getRate(), firstElement2.getRate());
        assertEquals(firstElement1.getCurrency(), firstElement2.getCurrency());

        firstElement1.setRate(firstElement1.getRate().multiply(BigDecimal.TEN));
        assertNotEquals(firstElement1.getRate(), firstElement2.getRate());

    }

    @Order(3)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"nbuq", "nbuccy", "cb", "*",})
    @DisplayName("load currencies with wrong service name")
    void testLoadCcyListFromNotExistRemoteServices(String wrongServiceName) throws Exception {
        String remoteUrl = GET_CCY_LIST_REMOTE_API.formatted(wrongServiceName);

        String nullRequestResult = mockMvc.perform(get(remoteUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CcyResponseDto> responseDtoList = objectMapper.readValue(nullRequestResult, new TypeReference<>() {
        });

        List<NbuCcyEntity> nbyCcyList = nbuRepository.findAll();
        List<CbuCcyEntity> cbyCcyList = cbuRepository.findAll();

        int webResponseListSize = responseDtoList.size();
        int cbuListSize = cbyCcyList.size();
        int nbuListSize = nbyCcyList.size();

        assertThatList(responseDtoList).isEmpty();
        assertThatList(cbyCcyList).isEmpty();
        assertThatList(nbyCcyList).isEmpty();

        assertNotEquals(webResponseListSize + 1, webResponseListSize);
        assertNotEquals(webResponseListSize + 1, cbuListSize);
        assertNotEquals(webResponseListSize + 1, nbuListSize);
        assertEquals(webResponseListSize, cbuListSize);
        assertEquals(webResponseListSize, nbuListSize);
        assertEquals(cbuListSize, nbuListSize);
    }

    @Order(4)
    @Transactional
    @ParameterizedTest
    @MethodSource("listOfCbuCcy")
    @DisplayName("Find rate of cbu currency with 200")
    void testCbuCcyAverageRateWith200(String cbuCcyName) throws Exception {
        listOfCbuCcyObj().stream()
                .map(item -> cbuCcyMapper.toEntity(item))
                .forEach(item -> cbuRepository.save(item));

        String url1 = GET_AVR_CCY_API.formatted("cbu", cbuCcyName);
        String url2 = GET_AVR_CCY_API.formatted("cbu_ccy", cbuCcyName);
        String url3 = GET_AVR_CCY_API.formatted("cbu_currency", cbuCcyName);

        String result1 = mockMvc.perform(get(url1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String result2 = mockMvc.perform(get(url2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String result3 = mockMvc.perform(get(url3))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto response1 = objectMapper.readValue(result1, new TypeReference<>() {
        });
        CcyResponseDto response2 = objectMapper.readValue(result2, new TypeReference<>() {
        });
        CcyResponseDto response3 = objectMapper.readValue(result3, new TypeReference<>() {
        });

        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response3).isNotNull();
        assertThat(response1.getRate()).isEqualTo(response2.getRate());
        assertThat(response1.getTitle()).isEqualTo(response3.getTitle());
        assertThat(response1.getTitle()).isEqualTo(response3.getTitle());
        assertThat(response2.getDate()).isEqualTo(response3.getDate());

        Optional<CbuCcyRecuestDto> dto = listOfCbuCcyObj().stream()
                .filter(item -> item.getCurrency().equals(response1.getCurrency()))
                .findFirst();

        assertThat(dto).isNotEmpty();
        assertThat(dto.get().getRate()).isEqualTo(response1.getRate());
        assertThat(dto.get().getRate()).isEqualTo(response2.getRate());
        assertThat(dto.get().getRate()).isEqualTo(response3.getRate());
    }


    @Order(5)
    @Transactional
    @ParameterizedTest
    @MethodSource("listOfNbuCcy")
    @DisplayName("Find rate of NBU currency with 200")
    void testNbuCcyAverageRateWith200(String nbuCcyName) throws Exception {
        listOfNbuCcyObj().stream()
                .map(item -> nbuCcyMapper.toEntity(item))
                .forEach(item -> nbuRepository.save(item));

        String url1 = GET_AVR_CCY_API.formatted("nbu", nbuCcyName);
        String url2 = GET_AVR_CCY_API.formatted("nbu_ccy", nbuCcyName);
        String url3 = GET_AVR_CCY_API.formatted("nbu_currency", nbuCcyName);

        String result1 = mockMvc.perform(get(url1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String result2 = mockMvc.perform(get(url2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String result3 = mockMvc.perform(get(url3))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto response1 = objectMapper.readValue(result1, new TypeReference<>() {
        });
        CcyResponseDto response2 = objectMapper.readValue(result2, new TypeReference<>() {
        });
        CcyResponseDto response3 = objectMapper.readValue(result3, new TypeReference<>() {
        });

        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response3).isNotNull();
        assertThat(response1.getRate()).isEqualTo(response2.getRate());
        assertThat(response1.getTitle()).isEqualTo(response3.getTitle());
        assertThat(response1.getTitle()).isEqualTo(response3.getTitle());
        assertThat(response2.getDate()).isEqualTo(response3.getDate());

        Optional<NbuCcyRecuestDto> dto = listOfNbuCcyObj().stream()
                .filter(item -> item.getCurrency().equals(response1.getCurrency()))
                .findFirst();

        assertThat(dto).isNotEmpty();
        assertThat(dto.get().getRate()).isEqualTo(response1.getRate());
        assertThat(dto.get().getRate()).isEqualTo(response2.getRate());
        assertThat(dto.get().getRate()).isEqualTo(response3.getRate());
    }

    @Order(6)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"nbu", "nbu_ccy", "nbu_currency", "cbu", "cbu_ccy", "cbu_currency"})
    @DisplayName("Find not exist currency rate")
    void testServicesWithWrongCcy(String serviceName) throws Exception {
        String ccy = randomGcyGenerator();
        String url = GET_AVR_CCY_API.formatted(serviceName, ccy);

        String result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto dto = objectMapper.readValue(result, new TypeReference<>() {
        });
        assertThat(result).isNotNull();
        assertThat(dto).isNotNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getCurrency()).isNull();
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getRate()).isEqualTo(BigDecimal.ZERO);
    }

    @Order(7)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"nbuz", "nbuccy", "nbe", "dcs", "*", "qwd12"})
    @DisplayName("Find not exist currency rate with not exist service")
    void testNotExistServicesWithWrongCcy(String serviceName) throws Exception {
        String ccy = randomGcyGenerator();
        String url = GET_AVR_CCY_API.formatted(serviceName, ccy);

        String result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto dto = objectMapper.readValue(result, new TypeReference<>() {
        });
        System.out.println(dto);
        assertThat(result).isNotNull();
        assertThat(dto).isNotNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getCurrency()).isEqualTo(ccy);
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getRate()).isEqualTo(BigDecimal.ZERO);
    }

    @Order(8)
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {"nbuz", "nbuccy", "nbe", "dcs", "*", "qwd12"})
    @DisplayName("Find exist currency rate with not exist service")
    void testNotExistServicesWithCcy(String serviceName) throws Exception {
        listOfNbuCcyObj().stream()
                .map(item -> nbuCcyMapper.toEntity(item))
                .forEach(item -> nbuRepository.save(item));

        listOfCbuCcyObj().stream()
                .map(item -> cbuCcyMapper.toEntity(item))
                .forEach(item -> cbuRepository.save(item));

        int listSize = allCcy().size();
        int index = new Random().nextInt(0, listSize);
        String ccy = allCcy().get(index);
        String url = GET_AVR_CCY_API.formatted(serviceName, ccy);

        String result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto dto = objectMapper.readValue(result, new TypeReference<>() {});
        System.out.println(dto);
        assertThat(result).isNotNull();
        assertThat(dto).isNotNull();
        assertThat(dto.getDate()).isNotNull();
        assertThat(dto.getCurrency()).isEqualTo(ccy);
        assertThat(dto.getTitle()).isNotNull();
        assertThat(dto.getRate()).isNotEqualTo(BigDecimal.ZERO);
    }

    @Order(9)
    @Transactional
    @ParameterizedTest
    @MethodSource("allCcy")
    @DisplayName("Calculate rate of exist currencies")
    void testCalculateAverageRateWithExistCcy(String currency) throws Exception {
        listOfNbuCcyObj().stream()
                .map(item -> nbuCcyMapper.toEntity(item))
                .forEach(item -> nbuRepository.save(item));

        listOfCbuCcyObj().stream()
                .map(item -> cbuCcyMapper.toEntity(item))
                .forEach(item -> cbuRepository.save(item));

        String url = GET_AVR_CCY_APIS.formatted(currency);

        String result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CcyResponseDto dto = objectMapper.readValue(result, new TypeReference<>() {});
        assertThat(result).isNotNull();
        assertThat(dto).isNotNull();
        assertThat(dto.getDate()).isNotNull();
        assertThat(dto.getCurrency()).isEqualTo(currency);
        assertThat(dto.getTitle()).isNotNull();
        assertThat(dto.getRate()).isNotEqualTo(BigDecimal.ZERO);

        Optional<CbuCcyRecuestDto> cbu = listOfCbuCcyObj().stream().filter(item -> item.getCurrency().equals(currency)).findFirst();
        Optional<NbuCcyRecuestDto> nbu = listOfNbuCcyObj().stream().filter(item -> item.getCurrency().equals(currency)).findFirst();

        assertThat(cbu).isNotEmpty();
        assertThat(nbu).isNotEmpty();
        assertThat(dto.getRate()).isEqualTo(cbu.get().getRate().add(nbu.get().getRate()).divide(BigDecimal.valueOf(2)));
    }

    String randomGcyGenerator() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int raqam = random.nextInt(65, 91);
            char a = (char) raqam;
            result.append(a);
        }
        return result.toString();
    }

    static List<CbuCcyRecuestDto> listOfCbuCcyObj() {
        return List.of(
                CbuCcyRecuestDto.builder().id(69).code("840").currency("USD").nameRussian("Доллар США").nameUzbek("AQSH dollari").nameUzbekKrill("АҚШ доллари").nameEnglish("US Dollar").nominal("1").rate(BigDecimal.valueOf(1223.11)).difference("-17.93").date("13.11.2023").build(),
                CbuCcyRecuestDto.builder().id(21).code("9832").currency("EUR").nameRussian("Евро").nameUzbek("EVRO").nameUzbekKrill("EВРО").nameEnglish("Euro").nominal("1").rate(BigDecimal.valueOf(2323.00)).difference("-44.95").date("13.11.2023").build(),
                CbuCcyRecuestDto.builder().id(57).code("345").currency("RUB").nameRussian("Российский рубль").nameUzbek("Rossiya rubli").nameUzbekKrill("Россия рубли").nameEnglish("Russian Ruble").nominal("1").rate(BigDecimal.valueOf(345.23)).difference("-17.93").date("13.11.2023").build()
        );
    }

    static List<NbuCcyRecuestDto> listOfNbuCcyObj() {
        return List.of(
                NbuCcyRecuestDto.builder().title("AQSh dollari").rate(BigDecimal.valueOf(12270.11)).currency("USD").date("13.11.2023 11:00:01").build(),
                NbuCcyRecuestDto.builder().title("Rossiya rubli").rate(BigDecimal.valueOf(133.33)).currency("RUB").date("13.11.2023 11:00:01").build(),
                NbuCcyRecuestDto.builder().title("Yevro").rate(BigDecimal.valueOf(13100.80)).currency("EUR").date("13.11.2023 11:00:01").build()
        );
    }

    static List<String> allCcy() {
        List<String> ccyList = new ArrayList<>();
        ccyList.addAll(listOfNbuCcy());
        ccyList.addAll(listOfCbuCcy());
        return ccyList;
    }

    static List<String> listOfCbuCcy() {
        return listOfCbuCcyObj().stream().map(CbuCcyRecuestDto::getCurrency).toList();
    }

    static List<String> listOfNbuCcy() {
        return listOfNbuCcyObj().stream().map(NbuCcyRecuestDto::getCurrency).toList();
    }

}

