// Return with commas in between
var numberWithCommas = function (x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

var dataPack1 = [21000, 22000, 26000, 35000, 55000, 55000, 56000, 59000, 60000, 61000, 60100, 62000];
var dataPack2 = [1000, 1200, 1300, 1400, 1060, 2030, 2070, 4000, 4100, 4020, 4030, 4050];
var dates = ["May 1", "May 2", "May 3", "May 4", "May 5", "May 6",
    "May 7", "May 8", "May 9", "May 10", "May 11", "May 12"];

// Chart.defaults.global.elements.rectangle.backgroundColor = '#FF0000';

var bar_ctx = document.getElementById('bar-chart');
var bar_chart = new Chart(bar_ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [
                {
                    label: 'Bowser',
                    data: dataPack1,
                    backgroundColor: "rgba(55, 160, 225, 0.7)",
                    hoverBackgroundColor: "rgba(55, 160, 225, 0.7)",
                    hoverBorderWidth: 2,
                    hoverBorderColor: 'lightgrey'
                },
                {
                    label: 'Mario',
                    data: dataPack2,
                    backgroundColor: "rgba(225, 58, 55, 0.7)",
                    hoverBackgroundColor: "rgba(225, 58, 55, 0.7)",
                    hoverBorderWidth: 2,
                    hoverBorderColor: 'lightgrey'
                },
            ]
        },
        options: {
            animation: {
                duration: 10,
            },
            tooltips: {
                mode: 'label',
                callbacks: {
                    label: function (tooltipItem, data) {
                        return data.datasets[tooltipItem.datasetIndex].label + ": " + numberWithCommas(tooltipItem.yLabel);
                    }
                }
            },
            scales: {
                xAxes: [{
                    stacked: true,
                    gridLines: {display: false},
                }],
                yAxes: [{
                    stacked: true,
                    ticks: {
                        callback: function (value) {
                            return numberWithCommas(value);
                        },
                    },
                }],
            }, // scales
            legend: {display: true}
        } // options
    }
);