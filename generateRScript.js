let title = "Trams vs Average passenger wait time"
let xaxis = "Trams"
let yaxis = "Average CS junction wait time (s)"

//let data_x = [12,14,16,18,20,22] 
//let data_y = [10.880116621299972,0.8000009092999959,2.2250261557999895,7.9751688418599915,39.7899322916,100]
//let err_y =  [0.18385021385860992, 0.06108871767653852, 0.12133696437383298, 0.31396656906263887, 1.9049836934537707, 0]

/*
let data_x = [12,14,16,18,20,22] 
let data_y = [175.163, 139.489, 124.17, 112.742, 104.872, 101.872]
let err_y = [0.24629016537790976, 0.12148139886092912, 0.11638751273305564, 0.12229549606640439, 0.22891662338874977, 0.13928017576581134]

*/

let data_x = [12,14,16,18,20,22]
let data_y = [14.425, 107.22, 105.335, 106.692, 184.254, 244.993]
let err_y = [0.15649490925454562, 0.3372749912915002, 0.31877369049977117, 0.35955401173289525, 1.2151554934567355,  0.9409790287701624]

let xstr = "x = c(" + data_x.join(",") + "); "
let ystr = "y = c(" + data_y.join(",") + "); "

let ymaxl = []
let yminl = []

for (let key in err_y) {
  let plus = err_y[key]
  let ymin = data_y[key] - plus
  let ymax = data_y[key] + plus
  ymaxl.push(ymax)
  yminl.push(ymin) 
}

let yminstr = "ymin = c(" + yminl.join(',') + "); "
let ymaxstr = "ymax = c(" + ymaxl.join(',') + "); "

let cmd = "errbar(x,y,ymin,ymax,cap=0.05, main = '" + title + "'" + ", xlab = '" + xaxis + "', ylab = '" + yaxis + "')"

let chained = xstr + ystr + yminstr + ymaxstr + cmd 
console.log(chained)