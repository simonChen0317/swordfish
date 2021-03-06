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
package com.baifendian.swordfish.common.job.struct.node.common;

import com.baifendian.swordfish.common.job.struct.resource.ResourceInfo;

import java.util.ArrayList;
import java.util.List;

public class UdfsInfo {
  private String func;

  private String className;

  private List<ResourceInfo> libJars = new ArrayList<>();

  public String getFunc() {
    return func;
  }

  public void setFunc(String func) {
    this.func = func;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public List<ResourceInfo> getLibJars() {
    return libJars;
  }

  public void setLibJars(List<ResourceInfo> libJars) {
    this.libJars = libJars;
  }
}
