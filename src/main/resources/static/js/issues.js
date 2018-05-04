$(document).ready(
    function () {
        var table = $('#issues').DataTable({
            "ajax": 'ajax/issues'
        });
        $('#issues tbody').on('click', 'tr', function () {
            var data = table.row(this).data();
            window.open("details?origin=" + data[0] + "&reference=" + data[1], "Details Report");
        });
        $.get("ajax/priorityAggregation",
            function (aggregation) {
                new Chart($("#priority-distribution-chart"), {
                    type: 'line',
                    data: aggregation,
                    options: {
                        scales: {
                            xAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Build'
                                }
                            }],
                            yAxes: [{
                                stacked: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Number of Issues'
                                }
                            }]
                        }
                    }
                });
            });
        $.get("ajax/originAggregation",
            function (aggregation) {
                new Chart($("#origin-chart"), {
                    type: 'line',
                    data: {
                        "labels": ["#1", "#2", "#3"],
                        "datasets": [{
                            label: 'PMD',
                            fill: false,
                            "data": [2, 20, 26],
                            "backgroundColor": "#f5c6cb",
                            "borderColor": "#e5b6bb",
                        }, {
                            label: 'CheckStyle',
                            fill: false,
                            "data": [75, 82, 31],
                            "backgroundColor": "#ffeeba",
                            "borderColor": "#efdeaa",
                        }, {
                            label: 'FindBugs',
                            fill: false,
                            "data": [10, 52, 61],
                            "backgroundColor": "#b8daff",
                            "borderColor": "#a8caef",
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            xAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Build'
                                }
                            }],
                            yAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Number Of Issues'
                                }
                            }]
                        }
                    }
                });
            });
    }
);

