
var call = require("../../utils/request.js")
var help = require("../../utils/help.js")
Page({

  /**
   * 页面的初始数据
   */
  data: {
      // 数据列表
      list:[
        {
          id: 1,
          logo: "/img/key.jpg",
          goods: "书1包",
          name: "andy",
          mobile: "1234567890",
          addr: "四川省成都市成都七中A5 605教室"
        },
        {
          id: 2,
          logo: "/img/key.jpg",
          goods: "书2包",
          name: "andy",
          mobile: "1234567890",
          addr: ".四川省成都市成都七中A5 605教室"
        },
        {
          id: 3,
          logo: "/img/key.jpg",
          goods: "书3包",
          name: "andy",
          mobile: "1234567890",
          addr: "................................."
        },
        {
          id: 4,
          logo: "/img/key.jpg",
          goods: "书包4",
          name: "andy",
          mobile: "1234567890",
          addr: "................................."
        },
        {
          id: 5,
          logo: "/img/key.jpg",
          goods: "书5包",
          name: "andy",
          mobile: "1234567890",
          addr: "................................."
        },
        {
          id: 6,
          logo: "/img/key.jpg",
          goods: "书6包",
          name: "andy",
          mobile: "1234567890",
          addr: "................................."
        }
      ],

      // 输入框内容
      inputContent:'',
  },


  // 封装书输入框
  seekChange:function(e){
    this.data.inputContent = e.detail.value;
  },


  /**
   * 搜索按钮
   */
  seekThings:function(){
      call.getData('/wx/getLose?name='+this.data.inputContent,this.onSuccess, this.onFail);
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
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.seekThings();
  },

})