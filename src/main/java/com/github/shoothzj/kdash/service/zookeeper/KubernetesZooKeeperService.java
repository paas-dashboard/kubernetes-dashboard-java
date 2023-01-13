/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.shoothzj.kdash.service.zookeeper;

import com.github.shoothzj.kdash.module.zookeeper.CreateZooKeeperDashboardReq;
import com.github.shoothzj.kdash.module.zookeeper.CreateZooKeeperReq;
import com.github.shoothzj.kdash.service.KubernetesDeployService;
import com.github.shoothzj.kdash.service.KubernetesServiceService;
import com.github.shoothzj.kdash.service.KubernetesStatefulSetService;
import com.github.shoothzj.kdash.util.KubernetesUtil;
import com.github.shoothzj.kdash.util.ZookeeperUtil;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KubernetesZooKeeperService {

    @Autowired
    private KubernetesStatefulSetService statefulSetService;

    @Autowired
    private KubernetesDeployService deployService;

    @Autowired
    private KubernetesServiceService serviceService;

    public void createZooKeeper(String namespace, CreateZooKeeperReq req) throws ApiException {
        serviceService.createService(namespace, ZookeeperUtil.service(req));
        statefulSetService.createNamespacedStatefulSet(namespace, ZookeeperUtil.statefulSet(req));
    }

    public void deleteZooKeeper(String namespace, String name) throws ApiException {
        serviceService.deleteService(namespace, KubernetesUtil.name("zookeeper", name));
        statefulSetService.deleteStatefulSet(namespace, KubernetesUtil.name("zookeeper", name));
    }

    public void createDashboard(String namespace, CreateZooKeeperDashboardReq req) throws ApiException {
        serviceService.createService(namespace, ZookeeperUtil.dashboardService(req));
        deployService.createNamespacedDeploy(namespace, ZookeeperUtil.dashboardDeploy(req));
    }

    public void deleteDashboard(String namespace, String dashboardName) throws ApiException {
        serviceService.deleteService(namespace, KubernetesUtil.name("zookeeper-dashboard", dashboardName));
        deployService.deleteDeploy(namespace, KubernetesUtil.name("zookeeper-dashboard", dashboardName));
    }
}
