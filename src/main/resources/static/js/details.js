$(document).ready(
    function () {
        $.get("priorities", {id: "hello-world"},
            function (priorities) {
                new Chart($("#priority-chart"), {
                    type: 'doughnut',
                    data: {
                        labels: ["High", "Normal", "Low"],
                        datasets: [{
                            label: 'High priority, Normal priority, Low priority',
                            data: priorities,
                            backgroundColor: [
                                '#d24939',
                                '#f7f1da',
                                '#80afbf'
                            ],
                            borderColor: [
                                '#355564', '#355564', '#355564'
                            ]
                        }]
                    }
                });
            });
        $.get("categories", {id: "hello-world"},
            function (categories) {
                Chart.defaults.global.elements.rectangle.backgroundColor = '#f7f1da';
                Chart.defaults.global.elements.rectangle.borderColor = '#355564';
                Chart.defaults.global.elements.rectangle.borderWidth = 1;
                new Chart($("#categories-chart"), {
                    type: 'horizontalBar',
                    label: 'Categories',
                    data: categories,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            })

    });

