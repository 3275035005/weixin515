<template>
  <div class="app-container">
    <!--查询表单  :inline="true" 表示表单单行展示 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label=用户名>
        <el-input v-model="dataVo.username" placeholder="请输入用户名"/>
      </el-form-item>

      <el-form-item label="性别">
        <el-select v-model="dataVo.sex" clearable placeholder="请选择性别">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
      <el-button type="primary" @click="updateAndSave(null)">新增</el-button>
    </el-form>


    <!-- 表格 -->
    <el-table
      :data="list"
      border
      fit
      highlight-current-row>

      <el-table-column
        label="序号"
        width="60"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}  <!--序号,算法-->
        </template>
      </el-table-column>
      <el-table-column
        label="头像">
        <template #default="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.image"
            :preview-src-list="[scope.row.image]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名"/>
      <el-table-column prop="sex" label="性别">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.sex == '0'">女</el-tag>
          <el-tag v-if="scope.row.sex == '1'">男</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="role" label="权限">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.role == '0'">管理员</el-tag>
          <el-tag  v-if="scope.row.role == '1'">学生</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.status == '0'">禁用</el-tag>
          <el-tag  v-if="scope.row.status == '1'">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="created" label="创建时间"/>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="updateAndSave(scope.row)">修改</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 新增修改弹框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="30%">

      <el-form :model="formData" label-position="left" label-width="100px">

        <el-form-item label="姓名">
          <el-input v-model="formData.name" placeholder="请输入姓名"/>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="formData.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :action="filesUploadUrl"
            :show-file-list="false"
            :on-success="filesUploadSuccess">
            <img v-if="formData.image" :src="formData.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" placeholder="请选择状态">
            <el-option label="禁用" value="0"></el-option>
            <el-option label="正常" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="formData.sex" placeholder="请选择性别">
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="权限">
          <el-select v-model="formData.role" placeholder="请选择权限">
            <el-option label="学生" value="1"></el-option>
            <el-option label="管理员" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleBtn">确 定</el-button>
    </span>
    </el-dialog>

    <!-- 分页 -->
    <el-pagination
      @current-change="getList"
      :current-page="page"
      style="padding: 30px 0; text-align: center;"
      :page-size="limit"
      layout="total, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </div>
</template>
<script>
  import {pageQuery, deleteById, saveTeacher} from "@/api/user";

  export default {
    data() { // 定义变量和初始值
      return {
        list: null,// 查询之后接口返回集合
        page: 1,// 当前页
        limit: 9, // 每页记录数
        total: 100, // 总页数
        dataVo: {}, // 条件封装对象
        dialogVisible:false,
        formData:{},
        title:'',
        filesUploadUrl:'http://localhost:5880/file/upload',
      }
    },
    created() { // 页面渲染之前执行，一般调用method定义方法
      this.getList()
    },
    methods: { // 创建具体的方法
      // 查询
      getList(page = 1) {
        this.page = page; // 获取用户点击的页码赋值
        pageQuery(this.page, this.limit, this.dataVo)
          .then(response => {// 请求成功
            console.log(response);
            this.list = response.data.rows;
            this.total = response.data.total;
          })
      },

      // 上传成功头像执行的方法
      filesUploadSuccess(res) {
        this.formData.image = res
      },
      // 修改和更新功能
      updateAndSave(row){
        console.log(row)
        if(row == null){
          this.title = '新增';
          this.formData = {
            image:'',
          };
        }else{
          this.title = '修改'
          this.formData = row;
        }
        this.dialogVisible = true;
      },

      // 确认按钮
      handleBtn(){
        // 关闭弹窗
        this.dialogVisible = false;
        saveTeacher(this.formData)
          .then(response => {// 请求成功
            this.$message({
              type: 'success',
              message: '操作成功！'
            });
            this.getList();
          })
      },

      // 删除
      removeDataById(id) {

        this.$confirm('此操作将永久删除该讲师信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'

        }).then(() => { // 确定执行的方法
          deleteById(id)
            .then(response => { // 删除成功执行的方法
              if (response.success) {
                this.$message({
                  type: 'success',
                  message: '删除成功！'
                });
                // 刷新表格
                this.getList()
              } else {
                this.$message({
                  type: 'error',
                  message: '删除失败！'
                });
              }
            })

        })
      },
      // 清空按按钮执行的方法
      resetData() {
        // 第一步清空条件数据
        this.dataVo = {};
        this.getList();
      }
    }
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
