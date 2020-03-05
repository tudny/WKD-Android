// strefy czaswoe
function zone(time){
  time = time.split(':');

  console.log(time);

  if(time[0] == 00)
    time = time[1];
  else
    time = parseInt(time[0] * 60) + parseInt(time[1]);

  console.log(time);

  if(time >= 39)
    return 3;
  else if(time >= 20)
    return 2;
  else
    return 1;
}
// bilety okresowe
function seasonTicket(distance, season, name, oneWay, relief){
  /*
  Odległości: 5 róznych możliwości
  */
  if(distance <= 5)
    distance = 1;
  else if(distance >= 6 && distance <= 10)
    distance = 2;
  else if(distance >= 11 && distance <= 15)
    distance = 3;
  else if(distance >= 16 && distance <= 20)
    distance = 4;
  else
    distance = 5;
  /*
  Okres: tygodniowe -> season = 1
  Imienne: tylko imienne -> name = 1
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0 lub 50 -> relief = 0 or relief = 50
  */
  if(season == 1){
    if(name == 1){
      if(relief == 0 || relief == 50){
        if(relief == 0){
          if(distance == 1)
            price = (oneWay) ? 18.00 : 36.00;
          else if(distance == 2)
            price = (oneWay) ? 24.50 : 49.00;
          else if(distance == 3)
            price = (oneWay) ? 27.50 : 55.00;
          else if(distance == 4)
            price = (oneWay) ? 34.50 : 69.00;
          else
            price = (oneWay) ? 44.50 : 89.00;
        } else {
          if(distance == 1)
            price = (oneWay) ? 9.00 : 18.00;
          else if(distance == 2)
            price = (oneWay) ? 12.25 : 24.50;
          else if(distance == 3)
            price = (oneWay) ? 13.75 : 27.50;
          else if(distance == 4)
            price = (oneWay) ? 17.25 : 34.50;
          else
            price = (oneWay) ? 22.25 : 44.50;
        }
        return price + ' PLN';
      } else {
        return 'Bilet tygodniowy można kupić tylko wg. taryfy normalnej lub z ulgą handlową 50%';
      }
    } else {
      return 'Bilet tygodniowy musi być imienny';
    }
  }
  /*
  Okres: dwutygodniowe -> season = 2
  Imienne: tylko imienne -> name = 1
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0 lub 50 -> relief = 0 or relief = 50
  */
  if(season == 2){
    if(name == 1){
      if(relief == 0 || relief == 50){
        if(relief == 0){
          if(distance == 1)
            price = (oneWay) ? 33.50 : 67.00;
          else if(distance == 2)
            price = (oneWay) ? 45.00 : 90.00;
          else if(distance == 3)
            price = (oneWay) ? 48.50 : 97.00;
          else if(distance == 4)
            price = (oneWay) ? 64.00 : 128.00;
          else
            price = (oneWay) ? 82.50 : 165.00;
        } else {
          if(distance == 1)
            price = (oneWay) ? 16.75 : 33.50;
          else if(distance == 2)
            price = (oneWay) ? 22.50: 45.00;
          else if(distance == 3)
            price = (oneWay) ? 24.25 : 48.50;
          else if(distance == 4)
            price = (oneWay) ? 32.00 : 64.00;
          else
            price = (oneWay) ? 41.25 : 82.50;
        }
        return price + ' PLN';
      } else {
        return 'Bilet dwutygodniowy można kupić tylko wg. taryfy normalnej lub z ulgą handlową 50%';
      }
    } else {
      return 'Bilet dwutygodniowy musi być imienny';
    }
  }
  /*
  Okres: miesięczny -> season = 3
  Imienne: name = 1 or name = 0
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0, 33, 37, 49, 50, 51, 78, 80, 93
  */
  if(season == 3){
    if(name == 1){
      if(relief == 0 || relief == 33 || relief == 37 || relief == 49 || relief == 50 || relief == 51 || relief == 78 || relief == 80 || relief == 93 || relief == 999){
        if(relief == 0){
          if(distance == 1)
            price = (oneWay) ? 54.50 : 109.00;
          else if(distance == 2)
            price = (oneWay) ? 73.00 : 146.00;
          else if(distance == 3)
            price = (oneWay) ? 79.00 : 158.00;
          else if(distance == 4)
            price = (oneWay) ? 97.50 : 195.00;
          else
            price = (oneWay) ? 124.00 : 248.00;
        } else if(relief == 33){
          if(distance == 1)
            price = (oneWay) ? 36.52 : 73.03;
          else if(distance == 2)
            price = (oneWay) ? 48.91 : 97.82;
          else if(distance == 3)
            price = (oneWay) ? 52.93 : 105.86;
          else if(distance == 4)
            price = (oneWay) ? 65.33 : 130.65;
          else
            price = (oneWay) ? 83.08 : 166.16;
        } else if(relief == 37){
          if(distance == 1)
            price = (oneWay) ? 34.34 : 68.67;
          else if(distance == 2)
            price = (oneWay) ? 45.99 : 91.98;
          else if(distance == 3)
            price = (oneWay) ? 49.77 : 99.54;
          else if(distance == 4)
            price = (oneWay) ? 61.43 : 122.85;
          else
            price = (oneWay) ? 78.12 : 156.24;
        } else if(relief == 49){
          if(distance == 1)
            price = (oneWay) ? 27.80 : 55.59;
          else if(distance == 2)
            price = (oneWay) ? 37.23 : 74.46;
          else if(distance == 3)
            price = (oneWay) ? 40.29 : 80.58;
          else if(distance == 4)
            price = (oneWay) ? 49.73 : 99.45;
          else
            price = (oneWay) ? 63.24 : 126.48;
        } else if(relief == 50){
          if(distance == 1)
            price = (oneWay) ? 27.25 : 54.50;
          else if(distance == 2)
            price = (oneWay) ? 36.50 : 73.00;
          else if(distance == 3)
            price = (oneWay) ? 39.50 : 79.00;
          else if(distance == 4)
            price = (oneWay) ? 48.75 : 97.50;
          else
            price = (oneWay) ? 62.00 : 124.00;
        } else if(relief == 51){
          if(distance == 1)
            price = (oneWay) ? 26.71 : 53.41;
          else if(distance == 2)
            price = (oneWay) ? 35.77 : 71.54;
          else if(distance == 3)
            price = (oneWay) ? 38.71 : 77.42;
          else if(distance == 4)
            price = (oneWay) ? 47.78 : 95.55;
          else
            price = (oneWay) ? 60.76 : 121.52;
        } else if(relief == 78){
          if(distance == 1)
            price = (oneWay) ? 11.99 : 23.98;
          else if(distance == 2)
            price = (oneWay) ? 16.06 : 32.12;
          else if(distance == 3)
            price = (oneWay) ? 17.38 : 34.76;
          else if(distance == 4)
            price = (oneWay) ? 21.45 : 42.90;
          else
            price = (oneWay) ? 27.28 : 54.56;
        } else if(relief == 80){
          if(distance == 1)
            price = (oneWay) ? 10.90 : 21.80;
          else if(distance == 2)
            price = (oneWay) ? 14.60 : 29.20;
          else if(distance == 3)
            price = (oneWay) ? 15.80 : 31.60;
          else if(distance == 4)
            price = (oneWay) ? 19.50 : 39.00;
          else
            price = (oneWay) ? 24.80 : 49.60;
        } else if(relief == 93){
          if(distance == 1)
            price = (oneWay) ? 3.82 : 7.63;
          else if(distance == 2)
            price = (oneWay) ? 5.11 : 10.22;
          else if(distance == 3)
            price = (oneWay) ? 5.53 : 11.06;
          else if(distance == 4)
            price = (oneWay) ? 6.83 : 13.65;
          else
            price = (oneWay) ? 8.68 : 17.36;
        } else if(relief == 999){
            price = 14.81;
        }
        return price + ' PLN';
      } else {
        return 'Bilet miesięczny imienny można kupić tylko wg. taryfy normalnej lub z ulgą 33%, 37%, 49%, 50%, 51%, 78%, 80%, 93%';
      }
    }
  }
  /*
  Okres: kwartalny -> season = 4
  Imienne: name = 1 or name = 0
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0, 33, 37, 49, 50, 51, 78, 80, 93
  */
  if(season == 4){
    if(name == 1){
      if(relief == 0 || relief == 50){
        if(relief == 0){
          if(distance == 1)
            price = (oneWay) ? 136.00 : 272.00;
          else if(distance == 2)
            price = (oneWay) ? 182.00 : 364.00;
          else if(distance == 3)
            price = (oneWay) ? 199.50 : 399.00;
          else if(distance == 4)
            price = (oneWay) ? 244.00 : 488.00;
          else
            price = (oneWay) ? 310.50 : 621.00;
        } else if(relief == 50){
          if(distance == 1)
            price = (oneWay) ? 68.00 : 136.00;
          else if(distance == 2)
            price = (oneWay) ? 91.00 : 182.00;
          else if(distance == 3)
            price = (oneWay) ? 99.75 : 199.50;
          else if(distance == 4)
            price = (oneWay) ? 122.00 : 244.00;
          else
            price = (oneWay) ? 155.25 : 310.50;
        }
        return price + ' PLN';
      } else {
        return 'Bilet kwartalny imienny można kupić tylko wg. taryfy normalnej lub z ulgą 50%';
      }
    }
  }
  // miesieczny bezimienny
  if(season == 5){
    if(distance == 1)
      price = 163.00;
    else if(distance == 2)
      price = 217.00;
    else if(distance == 3)
      price = 245.00;
    else if(distance == 4)
      price = 293.00;
    else
      price = 374.00;
    return price + ' PLN';
  }
  // kwartalny bezimienny
  if(season == 6){
    if(distance == 1)
      price = 407.00;
    else if(distance == 2)
      price = 544.00;
    else if(distance == 3)
      price = 614.00;
    else if(distance == 4)
      price = 732.00;
    else
      price = 931.00;
    return price + ' PLN';
  }
  // miesieczny-weekendowy bezimienny
  if(season == 7){
    if(distance == 1)
      price = 37.00;
    else if(distance == 2)
      price = 48.00;
    else if(distance == 3)
      price = 56.00;
    else if(distance == 4)
      price = 66.00;
    else
      price = 86.00;
    return price + ' PLN';
  }
  // miesieczny ROWER - bezimienny
  if(season == 8){
    if(distance == 1)
      price = 51.50;
    else if(distance == 2)
      price = 51.50;
    else if(distance == 3)
      price = 51.50;
    else if(distance == 4)
      price = 51.50;
    else
      price = 51.50;
    return price + ' PLN';
  }
}
//
$('#reliefTwoButtton').click(function(){
  distance = $('#distance').text();
  time = $('#ticektTime').find(":selected").val();
  name = $('input[name=ticektName]:checked').val();
  oneWay = $('input[name=oneWay]:checked').val();
  relief = $('#reliefTwo').find(":selected").val();
  if(oneWay == '') oneWay = 0;

  if(distance != '' && time != '' && name != ''  && oneWay != ''  && relief != ''){
    distance = parseInt($('#distance').text());
    price = seasonTicket(distance, time, name, oneWay, relief);
    if(price)
      $('#timetable-price2').show().html(price);
    else
      $('#timetable-price2').show().html('Nie ma takiego biletu.');
  }
});
// jednorazowe
function singleTicket(zone, relief){
  console.log(zone,relief);
  if(zone == 1){
    if(relief == 0 || relief == 'luggage')
      price = 4.10;
    else if(relief == 33)
      price = 2.75;
    else if(relief == 37)
      price = 2.58;
    else if(relief == 49)
      price = 2.09;
    else if(relief == 50)
      price = 2.05;
    else if(relief == 51)
      price = 2.01;
    else if(relief == 78)
      price = 0.90;
    else if(relief == 80)
      price = 2.05;
    else if(relief == 85)
      price = 0.62;
    else if(relief == 93)
      price = 0.29;
    else if(relief == 95)
      price = 0.21;
    else if(relief == 100)
      price = 0.00;
    else if(relief == 'railwayman')
      price = 2.05;
    else if(relief == 'driver')
      price = 5.50;
    else if(relief == 'urban2')
      price = 2.26;
    else if(relief == 'urban3')
      price = 0.00;
    else
      price = 20.50;
  } else if(zone == 2){
    if(relief == 0 || relief == 'luggage')
      price = 5.50;
    else if(relief == 33)
      price = 3.69;
    else if(relief == 37)
      price = 3.47;
    else if(relief == 49)
      price = 2.81;
    else if(relief == 50)
      price = 2.75;
    else if(relief == 51)
      price = 2.70;
    else if(relief == 78)
      price = 1.21;
    else if(relief == 80)
      price = 2.05;
    else if(relief == 85)
      price = 0.83;
    else if(relief == 93)
      price = 0.39;
    else if(relief == 95)
      price = 0.28;
    else if(relief == 100)
      price = 0.00;
    else if(relief == 'railwayman')
      price = 2.05;
    else if(relief == 'driver')
      price = 6.70;
    else
      price = 27.50;
  } else if(zone == 3){
    if(relief == 0 || relief == 'luggage')
      price = 8.00;
    else if(relief == 33)
      price = 5.36;
    else if(relief == 37)
      price = 5.04;
    else if(relief == 49)
      price = 4.08;
    else if(relief == 50)
      price = 4.00;
    else if(relief == 51)
      price = 3.92;
    else if(relief == 78)
      price = 1.76;
    else if(relief == 80)
      price = 2.05;
    else if(relief == 85)
      price = 1.20;
    else if(relief == 93)
      price = 0.56;
    else if(relief == 95)
      price = 0.40;
    else if(relief == 100)
      price = 0.00;
    else if(relief == 'railwayman')
      price = 2.05;
    else if(relief == 'driver')
      price = 9;
    else
      price = 40.00;
  }
  console.log(zone, relief, price);
  return price;
}
