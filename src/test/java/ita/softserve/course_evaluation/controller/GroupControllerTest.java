package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.GroupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Volodymyr Maliarchuk
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {GroupController.class, SpringSecurityTestConfiguration.class})
public class GroupControllerTest {
    public static final String API_GROUPS_URL = "/api/v1/groups";

    private ObjectMapper mapper;

    private Group group1;
    private Group group2;

    private User student1;
    private User student2;

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mapper = new ObjectMapper();

        group1 = new Group();
        group1.setId(1L);
        group1.setGroupName("Group_1");

        group2 = new Group();
        group2.setId(2L);
        group2.setGroupName("Group_2");

        student1 = new User();
        student1.setId(1L);
        student1.setFirstName("Erik");
        student1.setLastName("Sparks");
        student1.setEmail("erik@testmail.com");
        student1.setRoles(Set.of(Role.ROLE_TEACHER, Role.ROLE_STUDENT));
        student1.setPassword("password");
        student1.setGroup(group1);

        student2 = new User();
        student2.setId(2L);
        student2.setFirstName("Harry");
        student2.setLastName("Sparks");
        student2.setEmail("harry@testmail.com");
        student2.setRoles(Set.of(Role.ROLE_TEACHER, Role.ROLE_STUDENT));
        student2.setPassword("password");
        student2.setGroup(group2);

    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser()
    void testGetAllGroups() throws Exception {

        List<GroupDto> groupDtos = GroupDtoMapper.entityToDto(List.of(group1, group2));
        when(groupService.getAll()).thenReturn(groupDtos);

        String actual = mockMvc.perform(get(API_GROUPS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].groupName").value("Group_1"))
                .andExpect(jsonPath("$[1].groupName").value("Group_2"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(groupDtos);
        assertEquals(expected, actual);
        verify(groupService, times(1)).getAll();

    }

    @Test
    void testGetGroupById() throws Exception {

        GroupDto groupDto = GroupDtoMapper.entityToDto(group1);
        when(groupService.getById(Mockito.anyLong())).thenReturn(groupDto);

        Long id = group1.getId();
        String actual = mockMvc.perform(get(API_GROUPS_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("groupName").value(group1.getGroupName()))
                .andExpect(status().isOk())
                .andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(groupDto), actual);
        verify(groupService, times(1)).getById(Mockito.anyLong());
        verifyNoMoreInteractions(groupService);
    }
}
