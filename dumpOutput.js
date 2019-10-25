const csv = require('csv-parser')
const fs = require('fs')
const path = require('path');
const shelljs = require('shelljs')

let targs = {
  12: "output-data-21-27-58(Tram=12)",
  14: "output-data-21-32-33(Trams=14)",
  16: "output-data-21-36-44(Trams=16)",
  18: "output-data-21-43-59(Trams=18)",
  20: "output-data-21-48-43(Trams=20)",
  22: "output-data-22-27-10(Trams=22)"
}

let targs_pincrease = {
  100: "output-data-23-08-17(Passengers=100%)",
  200: "output-data-23-03-31(Passengers=200%)",
  300: "output-data-22-57-53(Passengers=300%)",
  400: "output-data-22-53-27(Passengers=400%)",
  500: "output-data-22-42-43(Passengers=500%)",
  "root": "./results/Realistic Input Data Passenger Increase/"
}

let fileDay = "./results/Realistic Input Data Tram Frequency/output-data-22-27-10(Trams=22)/DayPerformanceTable.csv"
let filePeak = "./results/Realistic Input Data Tram Frequency/output-data-22-27-10(Trams=22)/PeakPerformanceTable.csv"

let n = 22;

fileDay = "./results/Realistic Input Data Tram Frequency/" + targs[n] + "/DayPerformanceTable.csv"
filePeak = "./results/Realistic Input Data Tram Frequency/" + targs[n] + "/PeakPerformanceTable.csv"

fileDay = "./output/output-data-00-26-41/DayPerformanceTable.csv"
filePeak = "./output/output-data-00-26-41/PeakPerformanceTable.csv"

fileDay = "./" + targs_pincrease.root + targs_pincrease[200] + "/DayPerformanceTable.csv"
filePeak = "./" + targs_pincrease.root + targs_pincrease[200] + "/PeakPerformanceTable.csv"

let resultsDay = []
let resultsPeak = []

let rootDir = "./results/"
let targetDir = "./outputObjects/"


/*
Change trams data

12
Day percentageDelayCS 10.880116621299972 0.18385021385860992
Day averageWaitingTime 175.163 0.24629016537790976
Day csWaitJunctionAverage 14.425 0.15649490925454562

14
Day percentageDelayCS 0.8000009092999959 0.06108871767653852
Day averageWaitingTime 139.489 0.12148139886092912
Day csWaitJunctionAverage 107.228 0.3372749912915002

16
Day percentageDelayCS 2.2250261557999895 0.12133696437383298
Day averageWaitingTime 124.17 0.11638751273305564
Day csWaitJunctionAverage 105.335 0.31877369049977117

18
Day percentageDelayCS 7.9751688418599915 0.31396656906263887
Day averageWaitingTime 112.742 0.12229549606640439
Day csWaitJunctionAverage 106.692 0.35955401173289525

20
Day percentageDelayCS 39.7899322916 1.9049836934537707
Day averageWaitingTime 104.872 0.22891662338874977
Day csWaitJunctionAverage 184.254 1.2151554934567355

22
Day percentageDelayCS 100 0
Day averageWaitingTime 101.872 0.13928017576581134
Day csWaitJunctionAverage 244.993 0.9409790287701624


*/


//run,totalPassengers,totalWaitingTime,
//averageWaitingTime,maxWaitingTime,csTotalDepartures,
//csTotalDelays,csTotalDelayTime,csMaximumDelay,
//csAverageDelay,csPercentageOfDelays,csTotalJunctionWaitingTime,
//csAverageJunctionWaitingTime,csMaximumJunctionWaitingTime,csJunctionArrivals,
//prTotalDepartures,prTotalDelays,prTotalDelayTime,
//prMaximumDelay,prAverageDelay,prPercentageOfDelays,
//prTotalJunctionWaitingTime,prAverageJunctionWaitingTime,prMaximumJunctionWaitingTime,
//prJunctionArrivals

function getData(dir) {
  let fileDay =  dir + "/DayPerformanceTable.csv"
  let filePeak = dir + "/PeakPerformanceTable.csv"



  function getVar(data, key, avg) {
    let sum = 0;
    for (let i = 0; i < data.length; i++) {

      sum += (data[i][key] - avg) * (data[i][key] - avg)
    }

    return sum / (data.length - 1);
  }

  let num = 0;

  function write(obj, name) {
    let mdir = targetDir + dir
    let f = targetDir + dir + "/" + name + ".jsonstr"
    console.log("mkdir", mdir)
    console.log("dumpfile",  f)
    shelljs.mkdir("-p", targetDir + dir)
    fs.writeFileSync(f, JSON.stringify(obj))
    //console.log(JSON.stringify(obj))
  }

  function parseData(data, name) {
    let sumPercentageDelayCS = 0;
    let sumPercentageDelayPR = 0;

    let sumAvgDelayCS = 0;
    let sumAvgDelayPR = 0

    let sumMaxDelayCS = 0;
    let sumMaxDelayPR = 0;

    let sumAverageWaitingTime = 0;
    let sumMaxWaitingTime = 0;

    let sumCSWaitJunctionAverage = 0;
    let sumCSWaitJunctionMax = 0;

    let sumPRWaitJunctionAverage = 0;
    let sumPRWaitJunctionMax = 0;



    for (let index in data) {
      let row = data[index]

      let percentageDelayCS = parseFloat(row[10])
      let percentageDelayPR = parseFloat(row[20])

      let avgDelayCS = parseFloat(row[9])
      let avgDelayPR = parseFloat(row[19])

      let maxDelayCS = parseFloat(row[8])
      let maxDelayPR = parseFloat(row[18])

      let averageWaitingTime = parseFloat(row[3])
      let maxWaitingTime = parseFloat(row[4])

      let csWaitJunctionAverage = parseFloat(row[12])
      let csWaitJunctionMax = parseFloat(row[13])

      let prWaitJunctionAverage = parseFloat(row[22])
      let prWaitJunctionMax = parseFloat(row[23])

      sumPercentageDelayPR += percentageDelayPR;
      sumPercentageDelayCS += percentageDelayCS;

      sumAvgDelayCS += avgDelayCS;
      sumAvgDelayPR += avgDelayPR;

      sumMaxDelayCS += maxDelayCS;
      sumMaxDelayPR += maxDelayPR;

      sumAverageWaitingTime += averageWaitingTime;
      sumMaxWaitingTime += maxWaitingTime;

      sumCSWaitJunctionAverage += csWaitJunctionAverage;
      sumCSWaitJunctionMax += csWaitJunctionMax;

      sumPRWaitJunctionMax += prWaitJunctionMax;
      sumPRWaitJunctionAverage += prWaitJunctionAverage;
    }

    let avgs = {}
    let len = data.length;

    avgs["percentageDelayCS"] = sumPercentageDelayCS / len
    avgs["percentageDelayPR"] = sumPercentageDelayPR / len
    avgs["avgDelayCS"] = sumAvgDelayCS / len;
    avgs["avgDelayPR"] = sumAvgDelayPR / len; 
    avgs["maxDelayCS"] = sumMaxDelayCS / len;
    avgs["maxDelayPR"] = sumMaxDelayPR / len;
    avgs["averageWaitingTime"] = sumAverageWaitingTime / len;
    avgs["maxWaitingTime"] = sumMaxWaitingTime / len;
    avgs["csWaitJunctionAverage"] = sumCSWaitJunctionAverage / len;
    avgs["csWaitJunctionMax"] = sumCSWaitJunctionMax / len;
    avgs["prWaitJunctionAverage"] = sumPRWaitJunctionAverage / len;
    avgs["prWaitJunctionMax"] = sumPRWaitJunctionMax / len;

    let variance = {}

    variance["percentageDelayCS"] = getVar(data, 10, avgs["percentageDelayCS"])
    variance["percentageDelayPR"] = getVar(data, 20, avgs["percentageDelayPR"])
    variance["avgDelayCS"] = getVar(data, 9, avgs["avgDelayCS"])
    variance["avgDelayPR"] = getVar(data, 19, avgs["avgDelayPR"])
    variance["maxDelayCS"] = getVar(data, 8, avgs["maxDelayCS"])
    variance["maxDelayPR"] = getVar(data, 18, avgs["maxDelayPR"])
    variance["averageWaitingTime"] = getVar(data, 3, avgs["averageWaitingTime"])
    variance["maxWaitingTime"] = getVar(data, 4, avgs["maxWaitingTime"])
    variance["csWaitJunctionAverage"] = getVar(data, 12, avgs["csWaitJunctionAverage"])
    variance["csWaitJunctionMax"] = getVar(data, 13, avgs["csWaitJunctionMax"])
    variance["prWaitJunctionAverage"] = getVar(data, 22, avgs["prWaitJunctionAverage"])
    variance["prWaitJunctionMax"] = getVar(data, 23, avgs["prWaitJunctionMax"])

    let obj = {}

    for (let key in variance) {
      let avg = avgs[key];
      let confd = 1.960 * Math.sqrt(variance[key] / (data.length - 1))
      //console.log(name + " " + key + " " + avg + " " + confd)
      obj[key] = {
        variance: variance[key],
        average: avg,
        confidence: confd
      }
    }

    write(obj, name)
  }



  fs.createReadStream(fileDay)
    .pipe(csv({separator: ","}))
    .on('data', (data) => resultsDay.push(data))
    .on('end', () => {  
      parseData(resultsDay, "Day")
    })

  fs.createReadStream(filePeak)
    .pipe(csv({separator: ","}))
    .on('data', (data) => resultsPeak.push(data))
    .on('end', () => {  
      parseData(resultsPeak, "Peak")
    })

}


function dirchk(dir) {
  fs.readdir(dir, function (err, files) {
     files.forEach(function (file, index) {
        // Make one pass and make the file complete
        var fromPath = path.join(dir, file);

        fs.stat(fromPath, function (error, stat) {
          if (stat.isFile()) {
            if (file == "PeakPerformanceTable.csv") {
              getData(dir)
            } else if (file == "DayPerformanceTable.csv") {
              // handled above 
            }
          }
            
          else if (stat.isDirectory())
            dirchk(fromPath)

        })
    })  
  })
}

dirchk(rootDir)