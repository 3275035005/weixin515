
var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
Page({
  data:{
    userInfo:{},
    content:'', // 公告内容
  },

    /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let info =  wx.getStorageSync("user")
    if(info == null || info == '' || info == undefined){
       wx.reLaunch({
         url: '/pages/login/index'
      })
    }
    // 获取公告信息
    call.getData('/wx/getContent',this.onSuccess, this.onFail);
  },

  onSuccess(res) {
    

    if(res.code == 20000){
      this.setData({
        content: res.data.row.content
    })
    }
  },

  onFail() {
    wx.hideLoading();
    help.show("网络请求超时,请稍后再试")
  },


})
