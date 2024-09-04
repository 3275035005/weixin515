<template>
  <div class="app-container">
    <!--查询表单  :inline="true" 表示表单单行展示 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label=物品名称>
        <el-input v-model="dataVo.name" placeholder="请输入物品名称"/>
      </el-form-item>
      <el-form-item label=联系方式>
        <el-input v-model="dataVo.phone" placeholder="请输入联系方式"/>
      </el-form-item>
      <el-form-item label="处理状态">
        <el-select v-model="dataVo.status" clearable placeholder="请选择处理状态">
          <el-option label="已处理" value="1"></el-option>
          <el-option label="未处理" value="0"></el-option>
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

      <el-table-column prop="name" label="物品名称"/>
      <el-table-column prop="phone" label="联系方式"/>
      <el-table-column prop="price" label="跑腿费"/>
      <el-table-column prop="address" label="收货地址"/>
      <el-table-column prop="userId" label="发布人">
        <template slot-scope="scope">
          <el-tag  v-for="item in userList" v-if="scope.row.userId == item.id">{{item.name}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="处理状态">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.status == '0'">未处理</el-tag>
          <el-tag  v-if="scope.row.status == '1'">已处理</el-tag>
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
        <el-form-item label="物品名称">
          <el-input v-model="formData.name" placeholder="请输入物品名称"/>
        </el-form-item>

        <el-form-item label="联系方式">
          <el-input v-model="formData.phone" placeholder="请输入联系方式"/>
        </el-form-item>
        <el-form-item label="跑腿费">
          <el-input v-model="formData.price" placeholder="请输入跑腿费"/>
        </el-form-item>
        <el-form-item label="收货地址">
          <el-input v-model="formData.address" placeholder="请输入收货地址"/>
        </el-form-item>

        <el-form-item label="发布人">
          <el-select v-model="formData.userId" placeholder="请选择发布人">
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="formData.status" placeholder="请选择处理状态">
            <el-option label="未处理" value="0"></el-option>
            <el-option label="已处理" value="1"></el-option>
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
  import {getAll} from "@/api/user";
  import errand from "@/api/errand";
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
        userList:[],
        title:'',

      }
    },
    created() { // 页面渲染之前执行，一般调用method定义方法
      this.getList()
      this.getUserList();
    },
    methods: { // 创建具体的方法
      // 查询
      getList(page = 1) {
        this.page = page; // 获取用户点击的页码赋值
        errand.pageQuery(this.page, this.limit, this.dataVo)
          .then(response => {// 请求成功
            console.log(response);
            this.list = response.data.rows;
            this.total = response.data.total;
          })
      },

      // 修改和更新功能
      updateAndSave(row){
        console.log(row)
        if(row == null){
          this.title = '新增';
          this.formData = {};
        }else{
          this.title = '修改'
          this.formData = row;
        }
        this.dialogVisible = true;
      },


      // 获取所有用户
      getUserList(){
        getAll()
          .then(response => {// 请求成功
            this.userList = response.data.rows;
          })

      },
      // 确认按钮
      handleBtn(){
        // 关闭弹窗
        this.dialogVisible = false;
        errand.saveTeacher(this.formData)
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
          errand.deleteById(id)
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
