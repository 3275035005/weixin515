<template>
  <div class="app-container">
    <!--查询表单  :inline="true" 表示表单单行展示 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label=公告内容>
        <el-input v-model="dataVo.content" placeholder="请输入公告内容"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
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

      <el-table-column prop="content" label="公告内容"/>
      <el-table-column prop="created" label="创建时间"/>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="updateAndSave(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 新增修改弹框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="30%">

      <el-form :model="formData" label-position="left" label-width="100px">
        <el-form-item label="公告内容">
          <el-input v-model="formData.content" placeholder="请输入公告内容"/>
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
  import notice from '@/api/notice'

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
      }
    },
    created() { // 页面渲染之前执行，一般调用method定义方法
      this.getList()
    },
    methods: { // 创建具体的方法
      // 查询
      getList(page = 1) {
        this.page = page; // 获取用户点击的页码赋值
        notice.pageQuery(this.page, this.limit, this.dataVo)
          .then(response => {// 请求成功
            console.log(response);
            this.list = response.data.rows;
            this.total = response.data.total;
          })
      },

      // 修改和更新功能
      updateAndSave(row){
        this.title = '修改';
        this.formData = row;
        this.dialogVisible = true;
      },

      // 确认按钮
      handleBtn(){
        // 关闭弹窗
        this.dialogVisible = false;
        notice.saveTeacher(this.formData)
          .then(response => {// 请求成功
            this.$message({
              type: 'success',
              message: '操作成功！'
            });
            this.getList();
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
