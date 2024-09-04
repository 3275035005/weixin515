var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [
      {
        id: 1,
        goods: "小辣棒",
        addr: "5舍一栋",
        sex: "男",
        name: "唐一",
        avatarurl: "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=274612981,3403090092&fm=27&gp=0.jpg",
        contact: "15520449931",
        money: 3,
        time: "2019-3-19 16:35:25",
        status: 1
      },
      {
        id: 2,
        goods: "大辣条",
        addr: "6舍一栋",
        sex: "女",
        name: "梅花",
        avatarurl: "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=274612981,3403090092&fm=27&gp=0.jpg",
        contact: "15520449931",
        money: 3,
        time: "2019-3-19 13:35:25",
        status: 0
        
      },
      {
        id: 3,
        goods: "大面筋",
        addr: "6舍一栋",
        sex: "女",
        name: "梅花",
        avatarurl: "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=274612981,3403090092&fm=27&gp=0.jpg",
        contact: "15520449931",
        money: 3,
        time: "2019-3-19 09:35:25",
        status: 0
      }
    ],
    
  },
  callphone:function(e){
    wx.makePhoneCall({
      phoneNumber: e.currentTarget.dataset.num,
    })
  },

  // 获取跑腿数据
  getAll(){
    call.getData('/wx/getErrand',this.onSuccess, this.onFail);
  },

  onSuccess(res) {
    if(res.code == 20000){
      this.setData({
        list:res.data.rows
    })
    console.log(res.data.rows);
    }
  },
  onFail() {
    help.show("网络请求超时,请稍后再试")
  },


  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
      this.getAll();
  }

})