# 2023秋DevOps课程第2次实践作业报告

<center>第10组 OpsGenius</center>

## 承载平台

使用南软云完成作业

## 项目功能

I23o6：用户注册与登录

注册：
![](https://box.nju.edu.cn/f/b470b451fe55477d9b18/?dl=1)

登录：
![](https://box.nju.edu.cn/f/473dc756e1604fc88102/?dl=1)

## 服务端项目

### 构建与测试（单元测试）

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/pipelines/82326/

构建：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/162715

#### 加分项

##### 将单元测试报告整合到网页端

实现方式：在.gitlab-ci.yml文件中的Job的reports下进行配置，生成测试报告xml文件并通过GitLab展示

![](https://box.nju.edu.cn/f/ca2e18a3425c428cb5f3/?dl=1)

### 代码质量检查

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/pipelines/82326/

代码质量：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/162968

#### 加分项

##### 检测出除了TODO以外的代码质量问题

实现方式：在.gitlab-ci.yml文件中配置代码质量检查

![](https://box.nju.edu.cn/f/df6863725d68454d8c8b/?dl=1)

### 代码质量门禁

#### 执行记录链接

合并请求：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/merge_requests/5

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/pipelines/82812

代码质量检查：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/165837

#### 加分项

无

### 代码依赖检查与静态安全检查

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/pipelines/82326/

代码依赖检查：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/162900

静态安全检查：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/162716

#### 加分项

##### 静态安全检查检测出具体的问题并修复

实现方式：在.gitlab-ci.yml文件中配置了SAST检查并修改了有安全漏洞的代码（第三张图中的漏洞为其他修改导致，可以看到之前的漏洞已修复）

修改前：
![](https://box.nju.edu.cn/f/ae2f4b433e2841b3b9d2/?dl=1)

代码变更：
![](https://box.nju.edu.cn/f/bd691340165d4ea7bf74/?dl=1)

修改后：
![](https://box.nju.edu.cn/f/6e2e17eb449b4257bc32/?dl=1)

### 项目部署

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/pipelines/82326/

部署：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_server/-/jobs/162720

#### 加分项

##### 使用Docker进行部署

实现方式：在在.gitlab-ci.yml文件中配置以使用Docker部署

![](https://box.nju.edu.cn/f/befa265a0c0042838012/?dl=1)

##### yml中不出现具体凭据

实现方式：在GitLab中设置隐藏yml变量

![](https://box.nju.edu.cn/f/e107f381ef454e5e9f20/?dl=1)

## 网页端项目

### 构建

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/pipelines/82771

构建：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165458

#### 加分项

无

### 代码质量检查

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/pipelines/82771

代码质量检查：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165462

#### 加分项

##### 检测出除了TODO以外的代码质量问题

实现方式：在.gitlab-ci.yml文件中配置代码质量检查

代码质量检查:
![](https://box.nju.edu.cn/f/12c3bb90d0314930930c/?dl=1)

### 自动格式化

#### 执行记录链接

格式化前：
![](https://box.nju.edu.cn/f/eca3826bf2cc4e588a19/?dl=1)

格式化后：
![](https://box.nju.edu.cn/f/83c16aa966a44ce3aa63/?dl=1)

#### 加分项

无

### 代码依赖检查与静态安全检查

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/pipelines/82771

代码依赖检查：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165462

静态安全检查：

Nodejs：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165459

Semgrep：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165460

#### 加分项

无

### 项目部署

#### 执行记录链接

流水线：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/pipelines/82771

部署：
https://git.nju.edu.cn/10_2023_fall_devops/10_2023_fall_devops_web/-/jobs/165463

#### 加分项

##### 使用Docker进行部署

实现方式：在在.gitlab-ci.yml文件中配置以使用Docker部署

![](https://box.nju.edu.cn/f/befa265a0c0042838012/?dl=1)

##### yml中不出现具体凭据

实现方式：在GitLab中设置隐藏yml变量

![](https://box.nju.edu.cn/f/e107f381ef454e5e9f20/?dl=1)
