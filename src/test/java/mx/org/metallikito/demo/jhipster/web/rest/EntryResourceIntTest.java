package mx.org.metallikito.demo.jhipster.web.rest;

import mx.org.metallikito.demo.jhipster.DemoJhipsterApp;
import mx.org.metallikito.demo.jhipster.domain.Entry;
import mx.org.metallikito.demo.jhipster.repository.EntryRepository;
import mx.org.metallikito.demo.jhipster.service.EntryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EntryResource REST controller.
 *
 * @see EntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoJhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class EntryResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EXCERCISE = 1;
    private static final Integer UPDATED_EXCERCISE = 2;

    private static final Integer DEFAULT_MEALS = 1;
    private static final Integer UPDATED_MEALS = 2;

    private static final Integer DEFAULT_ALCOHOL = 1;
    private static final Integer UPDATED_ALCOHOL = 2;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private EntryRepository entryRepository;

    @Inject
    private EntryService entryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEntryMockMvc;

    private Entry entry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryResource entryResource = new EntryResource();
        ReflectionTestUtils.setField(entryResource, "entryService", entryService);
        this.restEntryMockMvc = MockMvcBuilders.standaloneSetup(entryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entry = new Entry();
        entry.setDate(DEFAULT_DATE);
        entry.setExcercise(DEFAULT_EXCERCISE);
        entry.setMeals(DEFAULT_MEALS);
        entry.setAlcohol(DEFAULT_ALCOHOL);
        entry.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createEntry() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry

        restEntryMockMvc.perform(post("/api/entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entries = entryRepository.findAll();
        assertThat(entries).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entries.get(entries.size() - 1);
        assertThat(testEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEntry.getExcercise()).isEqualTo(DEFAULT_EXCERCISE);
        assertThat(testEntry.getMeals()).isEqualTo(DEFAULT_MEALS);
        assertThat(testEntry.getAlcohol()).isEqualTo(DEFAULT_ALCOHOL);
        assertThat(testEntry.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setDate(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isBadRequest());

        List<Entry> entries = entryRepository.findAll();
        assertThat(entries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntries() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entries
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].excercise").value(hasItem(DEFAULT_EXCERCISE)))
                .andExpect(jsonPath("$.[*].meals").value(hasItem(DEFAULT_MEALS)))
                .andExpect(jsonPath("$.[*].alcohol").value(hasItem(DEFAULT_ALCOHOL)))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getEntry() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.excercise").value(DEFAULT_EXCERCISE))
            .andExpect(jsonPath("$.meals").value(DEFAULT_MEALS))
            .andExpect(jsonPath("$.alcohol").value(DEFAULT_ALCOHOL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntry() throws Exception {
        // Initialize the database
        entryService.save(entry);

        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        Entry updatedEntry = new Entry();
        updatedEntry.setId(entry.getId());
        updatedEntry.setDate(UPDATED_DATE);
        updatedEntry.setExcercise(UPDATED_EXCERCISE);
        updatedEntry.setMeals(UPDATED_MEALS);
        updatedEntry.setAlcohol(UPDATED_ALCOHOL);
        updatedEntry.setNotes(UPDATED_NOTES);

        restEntryMockMvc.perform(put("/api/entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEntry)))
                .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entries = entryRepository.findAll();
        assertThat(entries).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entries.get(entries.size() - 1);
        assertThat(testEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEntry.getExcercise()).isEqualTo(UPDATED_EXCERCISE);
        assertThat(testEntry.getMeals()).isEqualTo(UPDATED_MEALS);
        assertThat(testEntry.getAlcohol()).isEqualTo(UPDATED_ALCOHOL);
        assertThat(testEntry.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deleteEntry() throws Exception {
        // Initialize the database
        entryService.save(entry);

        int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Get the entry
        restEntryMockMvc.perform(delete("/api/entries/{id}", entry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Entry> entries = entryRepository.findAll();
        assertThat(entries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
