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
package com.baifendian.swordfish.webserver.api.service;

import com.baifendian.swordfish.common.config.BaseConfig;
import com.baifendian.swordfish.common.hadoop.HdfsClient;
import com.baifendian.swordfish.dao.mapper.ProjectMapper;
import com.baifendian.swordfish.dao.mapper.ResourceMapper;
import com.baifendian.swordfish.dao.model.Project;
import com.baifendian.swordfish.dao.model.Resource;
import com.baifendian.swordfish.dao.model.User;
import com.baifendian.swordfish.webserver.api.service.storage.FileSystemStorageService;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class ResourceService {

  private static Logger logger = LoggerFactory.getLogger(ResourceService.class.getName());

  @Value("${max.file.size}")
  private long maxFileSize;

  @Autowired
  private ResourceMapper resourceMapper;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ProjectMapper projectMapper;

  @Autowired
  FileSystemStorageService fileSystemStorageService;

  /**
   * 创建资源
   *
   * @param operator
   * @param projectName
   * @param name
   * @param desc
   * @param file
   * @param response
   * @return
   */
  @Transactional(value = "TransactionManager")
  public Resource createResource(User operator,
                                 String projectName,
                                 String name,
                                 String desc,
                                 MultipartFile file,
                                 HttpServletResponse response) {

    // 判断文件大小是否符合
    if (file.isEmpty() || file.getSize() > maxFileSize * 1024 * 1024) {
      response.setStatus(HttpStatus.SC_REQUEST_TOO_LONG);
      return null;
    }

    // 判断是否具备相应的权限, 必须具备写权限
    Project project = projectMapper.queryByName(projectName);

    if (project == null) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST);
      return null;
    }

    if (!projectService.hasWritePerm(operator.getId(), project)) {
      response.setStatus(HttpStatus.SC_UNAUTHORIZED);
      return null;
    }

    // 判断文件是否已经存在
    Resource resource = resourceMapper.queryResource(name);

    if (resource != null) {
      response.setStatus(HttpStatus.SC_CONFLICT);
      return null;
    }

    // 保存到本地
    String dir = BaseConfig.getLocalResourceDir(project.getId());

    try {
      fileSystemStorageService.createDir(dir);
    } catch (IOException e) {
      logger.error("Create dir failed", e);
      throw new RuntimeException("Update failed");
    }

    String destFilename = dir + File.separator + name;

    fileSystemStorageService.store(file, destFilename);

    // 保存到 hdfs
    String hdfsDestFilename = BaseConfig.getHdfsResourcesDir(project.getId()) + File.separator + name;
    HdfsClient.getInstance().mkdir(BaseConfig.getHdfsResourcesDir(project.getId()));

    HdfsClient.getInstance().copy(destFilename, hdfsDestFilename, true, true);

    // 插入数据
    resource = new Resource();

    Date now = new Date();

    resource.setName(name);
    resource.setDesc(desc);
    resource.setOwnerId(operator.getId());
    resource.setOwner(operator.getName());
    resource.setProjectId(project.getId());

    resource.setCreateTime(now);
    resource.setModifyTime(now);

    int count = resourceMapper.insert(resource);

    if (count == 1) {
      response.setStatus(HttpStatus.SC_CREATED);
      return resource;
    }

    response.setStatus(HttpStatus.SC_CONFLICT);
    return null;
  }

  /**
   * 修改资源
   *
   * @param operator
   * @param projectName
   * @param name
   * @param desc
   * @param file
   * @param response
   * @return
   */
  public Resource modifyResource(User operator,
                                 String projectName,
                                 String name,
                                 String desc,
                                 MultipartFile file,
                                 HttpServletResponse response) {
    // 判断文件大小是否符合
    if (file != null && (file.isEmpty() || file.getSize() > maxFileSize * 1024 * 1024)) {
      response.setStatus(HttpStatus.SC_REQUEST_TOO_LONG);
      return null;
    }

    // 判断是否具备相应的权限
    Project project = projectMapper.queryByName(projectName);

    if (project == null) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST);
      return null;
    }

    if (!projectService.hasWritePerm(operator.getId(), project)) {
      response.setStatus(HttpStatus.SC_UNAUTHORIZED);
      return null;
    }

    // 判断文件是否已经存在
    Resource resource = resourceMapper.queryResource(name);

    if (resource != null) {
      response.setStatus(HttpStatus.SC_CONFLICT);
      return null;
    }

    // 上传 & 插入数据
    return null;
  }

  /**
   * 删除资源
   *
   * @param operator
   * @param projectName
   * @param name
   * @param response
   * @return
   */
  public Resource deleteResource(User operator,
                                 String projectName,
                                 String name,
                                 HttpServletResponse response) {
    // 删除资源 & 数据库记录
    return null;
  }
}
