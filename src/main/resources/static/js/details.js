$(document).ready(
    function () {
        Chart.defaults.global.elements.rectangle.backgroundColor = '#f7f1da';
        Chart.defaults.global.elements.rectangle.borderColor = '#355564';
        Chart.defaults.global.elements.rectangle.borderWidth = 1;

        $.get("ajax/priorities", {id: "hello-world"},
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
        $.get("ajax/categories", {id: "hello-world"},
            function (categories) {
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
        $.get("ajax/types", {id: "hello-world"},
            function (types) {
                new Chart($("#types-chart"), {
                    type: 'horizontalBar',
                    label: 'Types',
                    data: types,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            })

    });

