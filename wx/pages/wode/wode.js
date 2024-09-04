// pages/me/me.js
const app = getApp();
var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo:{},
     myShophelp:[],
     myFoundthing:[],
     myGoodsList:[]
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
   this.setData({
    userInfo: info
  })

   call.getData('/wx/getAll?id='+info.id,this.onSuccess, this.onFail);
  },

  onSuccess(res) {
    wx.hideLoading();
    if(res.code == 20000){
      this.setData({
        myShophelp: res.data.rows.eErrand,
        myLostGoods: res.data.rows.eLose,
        myGoodsList: res.data.rows.eSecondhand
      })
    }
  },

  onFail() {
    wx.hideLoading();
    help.show("网络请求超时,请稍后再试")
  },
})