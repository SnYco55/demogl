package com.example.demo;

import com.example.demo.entity.DepartmentEntity;
import com.example.demo.entity.FacultyEntity;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.FacultyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    @Test
    void getFaculties_shouldReturn200() throws Exception {
        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk());
    }

    @Test
    void getFaculties_shouldReturnFaculties() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("fs"))
                .andExpect(jsonPath("$[0].name").value("Faculté des Sciences"));
    }

    @Test
    void getFaculty_shouldReturn200() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(get("/faculties/fs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fs"))
                .andExpect(jsonPath("$.name").value("Faculté des Sciences"));
    }

    @Test
    void getFaculty_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/faculties/inexistant"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST
    // -------------------------------------------------------------------------

    @Test
    void createFaculty_shouldReturn201() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fs",
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("fs"))
                .andExpect(jsonPath("$.name").value("Faculté des Sciences"));
    }

    @Test
    void createFaculty_shouldTrimIdAndName() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "  fs  ",
                                    "name": "  Faculté des Sciences  "
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("fs"))
                .andExpect(jsonPath("$.name").value("Faculté des Sciences"));
    }

    @Test
    void createFaculty_shouldReturn400_whenIdIsNull() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": null,
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenIdIsEmpty() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "",
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenIdIsOnlySpaces() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "   ",
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fs",
                                    "name": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenNameIsEmpty() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fs",
                                    "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenNameIsOnlySpaces() throws Exception {
        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fs",
                                    "name": "   "
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenIdAlreadyExists() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fs",
                                    "name": "Autre Faculté"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFaculty_shouldReturn400_whenNameAlreadyExists() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "fpms",
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // PATCH
    // -------------------------------------------------------------------------

    @Test
    void updateFaculty_shouldReturn200() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Nouvelle Faculté"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fs"))
                .andExpect(jsonPath("$.name").value("Nouvelle Faculté"));
    }

    @Test
    void updateFaculty_shouldTrimName() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "  Nouvelle Faculté  "
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouvelle Faculté"));
    }

    @Test
    void updateFaculty_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(patch("/faculties/inexistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Nouvelle Faculté"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFaculty_shouldReturn400_whenNameAlreadyExists() throws Exception {
        createFaculty("fs", "Faculté des Sciences");
        createFaculty("fpms", "Faculté Polytechnique");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Faculté Polytechnique"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFaculty_shouldAllowSameName() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Faculté des Sciences"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fs"))
                .andExpect(jsonPath("$.name").value("Faculté des Sciences"));
    }

    @Test
    void updateFaculty_shouldReturn400_whenNameIsNull() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFaculty_shouldReturn400_whenNameIsEmpty() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFaculty_shouldReturn400_whenNameIsOnlySpaces() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(patch("/faculties/fs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "   "
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Test
    void deleteFaculty_shouldReturn204() throws Exception {
        createFaculty("fs", "Faculté des Sciences");

        mockMvc.perform(delete("/faculties/fs"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFaculty_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/faculties/inexistant"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFaculty_shouldReturn409_whenFacultyHasDepartments() throws Exception {
        FacultyEntity faculty = createFaculty(
                "fs",
                "Faculté des Sciences"
        );

        DepartmentEntity department = new DepartmentEntity();
        department.setId("informatique");
        department.setFaculty(faculty);

        departmentRepository.save(department);

        mockMvc.perform(delete("/faculties/fs"))
                .andExpect(status().isConflict());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private FacultyEntity createFaculty(String id, String name) {
        FacultyEntity faculty = new FacultyEntity();
        faculty.setId(id);
        faculty.setName(name);

        return facultyRepository.save(faculty);
    }
}