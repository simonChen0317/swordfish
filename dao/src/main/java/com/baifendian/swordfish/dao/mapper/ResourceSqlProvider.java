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
package com.baifendian.swordfish.dao.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceSqlProvider {

  private final String TABLE_NAME = "resources";

  /**
   * 插入 <p>
   *
   * @return sql 语句
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO("resources");

        VALUES("`name`", "#{resource.name}");
        VALUES("`desc`", "#{resource.desc}");
        VALUES("`owner`", "#{resource.ownerId}");
        VALUES("`project_id`", "#{resource.projectId}");
        VALUES("`create_time`", "#{resource.createTime}");
        VALUES("`modify_time`", "#{resource.modifyTime}");
      }
    }.toString();
  }

  /**
   * 查询资源详情 <p>
   *
   * @return sql 语句
   */
  public String queryDetail(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("r.*");
        SELECT("u.name as owner_name");
        SELECT("u2.name as last_publish_by_name");
        SELECT("p.name as project_name");
        SELECT("p.org_id as org_id");
        SELECT("o.name as org_name");
        FROM("resources r");
        INNER_JOIN("user as u on u.id = r.owner_id");
        LEFT_OUTER_JOIN("user as u2 on u2.id = r.last_publish_by");
        INNER_JOIN("project as p on r.project_id = p.id");
        LEFT_OUTER_JOIN("org as o on p.org_id = o.id");
        WHERE("r.id = #{resourceId}");
      }
    }.toString();
  }

  /**
   * 查询资源详情 <p>
   *
   * @return sql 语句
   */
  public String queryDetails(Set<Integer> resourceIds) {

    StringBuilder sb = new StringBuilder();
    sb.append("SELECT r.*, u.name as owner_name, p.name as project_name, p.org_id as org_id, o.name as org_name ");
    sb.append("FROM resources r ");
    sb.append("LEFT OUTER JOIN user as u on u.id = r.owner_id ");
    sb.append("LEFT OUTER JOIN project as p on r.project_id = p.id ");
    sb.append("LEFT OUTER JOIN org as o on p.org_id = o.id ");

    if (resourceIds != null && resourceIds.size() > 0) {
      sb.append("where r.id in(");
      sb.append(StringUtils.join(resourceIds, ","));
      sb.append(")");
    }

    return sb.toString();
  }

  public String queryByProject(Integer projectId) {
    return new SQL() {
      {
        SELECT("*");
        FROM(TABLE_NAME);
        WHERE("project_id = ${projectId}");
      }
    }.toString();
  }

  /**
   * 查询资源目录 <p>
   *
   * @return sql 语句
   */
  public String queryAllResourceDirRelation(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("dr.*");
        FROM("dir_relation as dr");
        LEFT_OUTER_JOIN("resources as r on dr.file_id = r.id");
        //WHERE("dr.type = " + FolderType.RESOURCE.getType());
        WHERE("r.project_id = #{projectId}");
      }
    }.toString();
  }

  /**
   * 删除某个资源 <p>
   *
   * @return sql 语句
   */
  public String deleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM("resources");
        WHERE("id = #{resourceId}");
      }
    }.toString();
  }

  /**
   * 删除项目的资源信息
   */
  public String deleteByProjectId(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM("resources");
        WHERE("project_id = #{projectId}");
      }
    }.toString();
  }

  /**
   *
   * @param parameter
   * @return
   */
  public String queryIdByNames(Map<String, Object> parameter) {
    List<String> resourceNames = (List<String>) parameter.get("resourceNames");
    StringBuffer buffer = new StringBuffer();

    if (resourceNames != null) {
      for (String resourceName : resourceNames) {
        if (buffer.length() > 0) {
          buffer.append(",");
        }
        buffer.append("\"" + resourceName + "\"");
      }
    }

    return new SQL() {
      {
        SELECT("id");
        FROM("resources");
        WHERE("project_id = #{projectId}");
        WHERE("name in (" + buffer.toString() + ")");
      }
    }.toString();
  }

  public String countByProject(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("count(1)");
        FROM("resources");
        WHERE("project_id = #{projectId}");
      }
    }.toString();
  }
}
