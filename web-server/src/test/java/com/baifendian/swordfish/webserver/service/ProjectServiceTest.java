/*
 * Copyright (C) 2017 Baifendian Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baifendian.swordfish.webserver.service;

//import com.baifendian.swordfish.common.consts.Constants;
//import com.baifendian.swordfish.dao.model.Project;
//import com.baifendian.swordfish.dao.model.ProjectUser;
//import com.baifendian.swordfish.dao.model.User;
//import com.baifendian.swordfish.mock.MockDataService;
//import com.baifendian.swordfish.webserver.RestfulApiApplication;
//import com.baifendian.swordfish.webserver.exception.NotFoundException;
//import com.baifendian.swordfish.webserver.exception.ParameterException;
//import com.baifendian.swordfish.webserver.exception.PermissionException;
//import com.baifendian.swordfish.webserver.exception.ServerErrorException;
//import org.apache.commons.httpclient.HttpStatus;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
//import static junit.framework.TestCase.assertTrue;
//
///**
// * projectService 单元测试
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = RestfulApiApplication.class)
//@WebAppConfiguration
//@Transactional
//public class ProjectServiceTest {
//
//  private static Logger logger = LoggerFactory.getLogger(ProjectServiceTest.class.getName());
//
//  @Autowired
//  private MockDataService mockDataService;
//
//  @Autowired
//  private ProjectService projectService;
//
//  // 模拟普通用户
//  private User user;
//
//  // 模拟管理员用户
//  private User userAdmin;
//
//  // 模拟项目1
//  private Project project;
//
//  @Before
//  public void setUp() throws Exception {
//    user = mockDataService.createGeneralUser();
//    project = mockDataService.createProject(user);
//    userAdmin = mockDataService.createAdminUser();
//  }
//
//  /**
//   * 创建项目测试
//   */
//  @Test
//  public void testCreateProject() {
//    {
//      // 正常创建一个项目
//      String name = mockDataService.getRandomString();
//      String desc = mockDataService.getRandomString();
//      Project res = projectService.createProject(user, name, desc);
//      assertTrue(res != null);
//    }
//
//    {
//      // 管理员创建项目
//      String name = mockDataService.getRandomString();
//      String desc = mockDataService.getRandomString();
//      boolean thrown = false;
//      try {
//        Project res = projectService.createProject(userAdmin, name, desc);
//      } catch (PermissionException e) {
//        thrown = true;
//      }
//      assertTrue(thrown);
//    }
//
//    {
//      // 重复创建project
//      String name = project.getName();
//      String desc = mockDataService.getRandomString();
//      boolean thrown = false;
//      try {
//        Project res = projectService.createProject(user, name, desc);
//      } catch (ServerErrorException e) {
//        thrown = true;
//      }
//      assertTrue(thrown);
//    }
//  }
//
//  @Test
//  public void testModifyProject() {
//    {
//      // 正常修改一个项目
//      String desc = mockDataService.getRandomString();
//      Project res = projectService.modifyProject(user, project.getName(), desc);
//      assertTrue(res != null);
//      assertEquals(res.getDesc(), desc);
//    }
//    {
//      // 非 owner 修改一个项目
//      String desc = mockDataService.getRandomString();
//      boolean thrown = false;
//      try {
//        Project res = projectService.modifyProject(userAdmin, project.getName(), desc);
//      } catch (PermissionException e) {
//        thrown = true;
//      }
//
//      assertTrue(thrown);
//    }
//  }
//
//  @Test
//  public void testDeleteProject() {
//    {
//      // 正常删除一个项目
//      Project project1 = mockDataService.createProject(user);
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      projectService.deleteProject(user, project1.getName());
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//    }
//    {
//      // 删除一个不是自己的项目
//      Project project1 = mockDataService.createProject(user);
//      boolean thrown = false;
//
//      try {
//        projectService.deleteProject(userAdmin, project1.getName());
//      } catch (PermissionException e) {
//        thrown = true;
//      }
//
//      assertTrue(thrown);
//    }
//
//    {
//      // 删除一个不存在的项目
//      String name = mockDataService.getRandomString();
//      boolean thrown = false;
//      try {
//        projectService.deleteProject(user, name);
//      } catch (NotFoundException e) {
//        thrown = true;
//      }
//
//      assertTrue(thrown);
//    }
//  }
//
//  @Test
//  public void testQueryProject() {
//    {
//      // 普通查询一个用户下所有项目
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      List<Project> projectList = projectService.queryProject(user);
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//      assertTrue(projectList != null);
//      assertTrue(projectList.size() > 0);
//    }
//
//    {
//      // 一个管理员查看所有项目
//      User user2 = mockDataService.createGeneralUser();
//      Project project1 = mockDataService.createProject(user2);
//
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      List<Project> projectList = projectService.queryProject(userAdmin);
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//      assertTrue(projectList != null);
//      assertTrue(projectList.size() >= 2);
//    }
//  }
//
//  @Test
//  public void testAddProjectUser() {
//    {
//      // 正常添加一个用户到项目中
//      int perm = Constants.PROJECT_USER_PERM_READ;
//      User user1 = mockDataService.createGeneralUser();
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      ProjectUser projectUser = projectService.addProjectUser(user, project.getName(), user1.getName(), perm);
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//      assertTrue(projectUser != null);
//      assertEquals(projectUser.getProjectId(), project.getId());
//      assertEquals(projectUser.getUserId(), user1.getId());
//    }
//
//    {
//      // 非项目所有者添加一个用户到项目中
//      int perm = 1;
//      User user1 = mockDataService.createGeneralUser();
//
//      boolean thrown = false;
//
//      try {
//        ProjectUser projectUser = projectService.addProjectUser(userAdmin, project.getName(), user1.getName(), perm);
//      } catch (PermissionException e) {
//        thrown = true;
//      }
//
//      assertTrue(thrown);
//    }
//
//    {
//      // 添加一个不存在的用户到项目中
//      int perm = 1;
//
//      String userName = mockDataService.getRandomString();
//      boolean thrown = false;
//
//      try {
//        ProjectUser projectUser = projectService.addProjectUser(user, project.getName(), userName, perm);
//      } catch (NotFoundException e) {
//        thrown = true;
//      }
//
//      assertTrue(thrown);
//    }
//
//    {
//      // 添加一个已经存在项目中的用户到项目中
//      int perm = 1;
//
//      try {
//        projectService.addProjectUser(user, project.getName(), user.getName(), perm);
//      } catch (Exception e) {
//        logger.error(e.getMessage(), e);
//      }
//    }
//  }
//
//  @Test
//  public void testDeleteProjectUser() {
//    {
//      // 正常删除一个项目中的用户
//      int perm = 1;
//      User user1 = mockDataService.createGeneralUser();
//      mockDataService.createProjectUser(project.getId(), user1.getId(), perm);
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      projectService.deleteProjectUser(user, project.getName(), user1.getName());
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//    }
//
//    {
//      // 非项目所有者删除一个用户
//      int perm = 1;
//      User user1 = mockDataService.createGeneralUser();
//      mockDataService.createProjectUser(project.getId(), user1.getId(), perm);
//
//      boolean res = false;
//      try {
//        projectService.deleteProjectUser(userAdmin, project.getName(), user1.getName());
//      } catch (PermissionException e) {
//        res = true;
//      }
//      assertTrue(res);
//    }
//
//    {
//      // 删除一个不存的的用户
//      String userName = mockDataService.getRandomString();
//
//      boolean res = false;
//
//      try {
//        projectService.deleteProjectUser(user, project.getName(), userName);
//      } catch (NotFoundException e) {
//        res = true;
//      }
//      assertTrue(res);
//    }
//
//    {
//      // 删除一个不存在项目中的用户
//      User user1 = mockDataService.createGeneralUser();
//      String userName = mockDataService.getRandomString();
//
//      boolean res = false;
//
//      try {
//        projectService.deleteProjectUser(user, project.getName(), user1.getName());
//      } catch (NotFoundException e) {
//        res = true;
//      }
//
//      assertTrue(res);
//    }
//
//    {
//      // 删除自己
//      boolean res = false;
//      try {
//        projectService.deleteProjectUser(user, project.getName(), user.getName());
//      } catch (ParameterException e) {
//        res = true;
//      }
//      assertTrue(res);
//    }
//  }
//
//  @Test
//  public void testQueryUser() {
//    {
//      // 正常查询所有用户
//      MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//      List<ProjectUser> projectUserList = projectService.queryUser(user, project.getName());
//      assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.SC_OK);
//      assertTrue(projectUserList != null);
//    }
//
//    {
//      // 非项目 owenr 查询
//      boolean res = false;
//      try {
//        projectService.queryUser(userAdmin, project.getName());
//      } catch (PermissionException e) {
//        res = true;
//      }
//      assertTrue(res);
//    }
//  }
//}
