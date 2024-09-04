/**
 * 登录
 * @version 1.0
 */
var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
Page({

    /**
     * 页面的初始数据
     */
    data: {
        username:'',     // 账号
        password:'',  // 密码
    },


    // 登录按钮
    loginBtn(){
      if(this.data.username == ""){
      wx.showToast({
          title: '账号不能为空',
          icon: 'none',
          duration: 1000
      })
      return false;
      }
      if(this.data.password == ""){
        wx.showToast({
          title: '密码不能为空',
          icon: 'none',
          duration: 1000
        })
        return false;
          }
          wx.showLoading({
            title: '正在登录...'
          });
        console.log(this.data.username);
        console.log(this.data.password);

        call.request('/wx/login', {username: this.data.username, password: this.data.password},this.onSuccess, this.onFail);

    },

    onSuccess(res) {
      wx.hideLoading();
      if(res.code == 20000){
          wx.setStorageSync('user',res.data.token)
          wx.reLaunch({
              url: '/pages/index/index'
          })
      }else{
          help.show(res.message)
      }
    },

    onFail() {
      wx.hideLoading();
      help.show("网络请求超时,请稍后再试")
    },


    //获取input输入框的值
    getUsername:function(e){
      this.setData({
        username:e.detail.value
      })
   },

   getPassword:function(e){
        this.setData({
            password:e.detail.value 
        })
    }
  
})