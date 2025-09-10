document.addEventListener('DOMContentLoaded', function() {
    // Task verileri
    if (window.location.pathname.includes('signin.html')) {
        const signInBtn = document.getElementById('signInBtn');
        const usernameInput = document.querySelector('input[type="text"]');

        if (signInBtn && usernameInput) {
            signInBtn.addEventListener('click', function () {
                const username = usernameInput.value.trim().toLowerCase();

                if (username === "admin") {
                    window.location.href = "admin.html";
                } else if (username === "restaurant") {
                    window.location.href = "restaurant.html";
                } else {
                    alert("Geçersiz kullanıcı adı. Lütfen 'admin' ya da 'restaurant' yaz.");
                }
            });
        }
    }
    // 👇 Diğer sayfa kodların (index.html) burada kalabilir
    const signInPageBtn = document.querySelector('.sign-in');
    if (signInPageBtn) {
        signInPageBtn.addEventListener('click', function () {
            window.location.href = 'signin.html';
        });
    }

    const signUpPageBtn = document.querySelector('.sign-up');
    if (signUpPageBtn) {
        signUpPageBtn.addEventListener('click', function () {
            window.location.href = 'signup.html';
        });
    }
    const tasks = [
        { id: 1, name: "Task 1", count: 16 },
        { id: 2, name: "Task 2", count: 8 },
        { id: 3, name: "Task 3", count: 24 },
        { id: 4, name: "Task 4", count: 5 },
        { id: 5, name: "Task 5", count: 12 }
    ];

    // Task'ları oluştur
    const tasksContainer = document.getElementById('tasksContainer');
    tasks.forEach(task => {
        const taskElement = document.createElement('div');
        taskElement.className = 'task';
        taskElement.innerHTML = `
            <div class="task-count">${task.count}</div>
            <div class="task-label">${task.name}</div>
        `;
        tasksContainer.appendChild(taskElement);
    });

    const cuisines = [
        { name: "Italian", img: "italian.jpg" },
        { name: "Mexican", img: "italian.jpg" },
        { name: "Japanese", img: "italian.jpg" },
        { name: "Indian", img: "italian.jpg" },
        { name: "Turkish", img: "italian.jpg" },
        { name: "Chinese", img: "italian.jpg" }
    ];    const cuisinesSection = document.querySelector('.cuisines');

    const cuisinesContainer = document.createElement('div');
    cuisinesContainer.className = 'tasks-container';
    cuisinesSection.appendChild(cuisinesContainer);

    cuisines.forEach(cuisine => {
        const img = document.createElement('img');
        img.src = cuisine.img;
        img.alt = cuisine.name;
        img.style.width = "100px";
        img.style.height = "100px";
        img.style.borderRadius = "10px";
        img.style.objectFit = "cover";
        cuisinesContainer.appendChild(img);
    });

    // Restoran verileri
    const restaurants = [
        { name: "Pizza Palace", rating: 4.5 },
        { name: "Burger King", rating: 4.2 },
        { name: "Sushi World", rating: 4.7 },
        { name: "Taco Fiesta", rating: 4.0 }
    ];
    const restaurantsSection = document.querySelector('.popular-restaurants');

    restaurants.forEach(restaurant => {
        const restaurantElement = document.createElement('div');
        restaurantElement.className = 'restaurant';
        restaurantElement.innerHTML = `
            <h3>${restaurant.name}</h3>
            <div>Rating: ${restaurant.rating} <i class="fas fa-star" style="color: gold;"></i></div>
        `;
        restaurantsSection.appendChild(restaurantElement);
    });

    // Auth butonlarına event listener ekle
    document.querySelector('.sign-up').addEventListener('click', function() {
        window.location.href = 'signup.html';
        // Burada sign up işlemleri yapılabilir

    });

    document.querySelector('.sign-in').addEventListener('click', function() {
        window.location.href = 'signin.html';
        // Burada sign in işlemleri yapılabilir


    });


});