var vm = new Vue({
    el:'#rrapp',
    data:{
        server:{},
        backServer:{
            "cpu":{
                "cpuNum":0,
                "free":0,
                "sys":0,
                "total":0,
                "used":0,
                "wait":0
            },
            "jvm":{
                "free":0,
                "home":"/",
                "max":0,
                "name":"Java HotSpot(TM) 64-Bit Server VM",
                "runTime":"0天0小时0分钟",
                "startTime":"2018-00-00 00:00:00",
                "total":0,
                "usage":0,
                "used":0,
                "version":"1.8.0_181"
            },
            "mem":{
                "free":0,
                "total":0,
                "usage":0,
                "used":0
            },
            "sys":{
                "computerIp":"localhost",
                "computerName":"default Name",
                "osArch":"x86_64",
                "osName":"Mac OS X",
                "userDir":"/"
            },
            "sysFiles":[
                {
                    "dirName":"/",
                    "free":"0 GB",
                    "sysTypeName":"default",
                    "total":"0 GB",
                    "typeName":"mac",
                    "usage":0,
                    "used":"0 GB"
                }
            ]
        }
    },
    created: function () {
        this.getInfo();
    },
    methods: {
        getInfo: function(){
            var _this = this
            axios.get(baseURL +'sys/server/getInfo')
                .then(function (res) {
                    console.log(res.data);
                    if (res.data.server !=null){
                        _this.server = res.data.server;
                        console.log(_this.server);
                    }else{
                        _this.server = _this.backServer;
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                });
        }
    }
});
