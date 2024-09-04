// pages/secondhandstore/secondhandstore.js
var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodsList: [],
    inputValue:"",
  },
  
  // 查看详情
  showDetail:function(e){
    let index = e.currentTarget.dataset.index;
    console.log(index);
    console.log(this.data.goodsList);
    let goods = this.data.goodsList[index];
    console.log();
    app.globalData.goodsdetail = goods;
    wx.navigateTo({
      url: './detail/detail',
    })
  },




  // 输入框
  seekChange:function(e){
    this.data.inputValue = e.detail.value

  },
  // 搜索框
  seekGoods:function(){
    call.getData('/wx/getSecondhand?name='+this.data.inputValue,this.onSuccess, this.onFail);
  },

  onSuccess(res) {
    console.log(res);
    if(res.code == 20000){
      this.setData({
        goodsList:res.data.rows
    })
    }
  },
  onFail() {
    help.show("网络请求超时,请稍后再试")
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.seekGoods();
  },
})