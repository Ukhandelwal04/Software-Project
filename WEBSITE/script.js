'use strict';

// modal variables
const modal = document.querySelector('[data-modal]');
const modalCloseBtn = document.querySelector('[data-modal-close]');
const modalCloseOverlay = document.querySelector('[data-modal-overlay]');

// modal function
const modalCloseFunc = function () { modal.classList.add('closed') }

// modal eventListener
modalCloseOverlay.addEventListener('click', modalCloseFunc);
modalCloseBtn.addEventListener('click', modalCloseFunc);





// notification toast variables
const notificationToast = document.querySelector('[data-toast]');
const toastCloseBtn = document.querySelector('[data-toast-close]');

// notification toast eventListener
toastCloseBtn.addEventListener('click', function () {
  notificationToast.classList.add('closed');
}); 





// mobile menu variables
const mobileMenuOpenBtn = document.querySelectorAll('[data-mobile-menu-open-btn]');
const mobileMenu = document.querySelectorAll('[data-mobile-menu]');
const mobileMenuCloseBtn = document.querySelectorAll('[data-mobile-menu-close-btn]');
const overlay = document.querySelector('[data-overlay]');

for (let i = 0; i < mobileMenuOpenBtn.length; i++) {

  // mobile menu function
  const mobileMenuCloseFunc = function () {
    mobileMenu[i].classList.remove('active');
    overlay.classList.remove('active');
  }

  mobileMenuOpenBtn[i].addEventListener('click', function () {
    mobileMenu[i].classList.add('active');
    overlay.classList.add('active');
  });

  mobileMenuCloseBtn[i].addEventListener('click', mobileMenuCloseFunc);
  overlay.addEventListener('click', mobileMenuCloseFunc);

}





// accordion variables
const accordionBtn = document.querySelectorAll('[data-accordion-btn]');
const accordion = document.querySelectorAll('[data-accordion]');

for (let i = 0; i < accordionBtn.length; i++) {

  accordionBtn[i].addEventListener('click', function () {

    const clickedBtn = this.nextElementSibling.classList.contains('active');

    for (let i = 0; i < accordion.length; i++) {

      if (clickedBtn) break;

      if (accordion[i].classList.contains('active')) {

        accordion[i].classList.remove('active');
        accordionBtn[i].classList.remove('active');

      }

    }

    this.nextElementSibling.classList.toggle('active');
    this.classList.toggle('active');

  });

}

document.addEventListener('DOMContentLoaded', function () {
  var hamburgerMenu = document.getElementById('hamburger-menu');
  var mobileMenu = document.getElementById('mobile-menu');
  var overlay = document.createElement('div');
  overlay.className = 'overlay';
  document.body.appendChild(overlay);

  hamburgerMenu.addEventListener('click', function () {
      mobileMenu.style.left = '0%';
      overlay.style.display = 'block';
  });

  overlay.addEventListener('click', function () {
      mobileMenu.style.left = '-100%';
      overlay.style.display = 'none';
  });

  document.getElementById('close').addEventListener('click', function () {
      mobileMenu.style.left = '-100%';
      overlay.style.display = 'none';
  });

  var menuLinks = document.querySelectorAll('#mobile-menu a');
  menuLinks.forEach(function (link) {
      link.addEventListener('click', function () {
          mobileMenu.style.left = '-100%';
          overlay.style.display = 'none';
      });
  });
});



var images = document.getElementById('images');
var zoom = document.getElementById('modal-zoom');
var xZoom = document.getElementById('icon-close');

images.addEventListener('click', function () {
  zoom.classList.remove('zoom');
});

xZoom.addEventListener('click', function () {
  zoom.classList.add('zoom');
});



$(document).ready(function () {
  $(".image-container").slick({
      infinite: true,
      slidesToShow: 1,
      slidesToScroll: 1,
      prevArrow: $(".prev-icon"),
      nextArrow: $(".next-icon"),
  });
});

var counterBtn = document.getElementById("counterBtn");
var counterValue = document.getElementById("counterValue");
var addToCartBtn = document.getElementById("addToCartBtn");
var cartAmount = document.getElementById("cartAmount");
var cartFilled = document.getElementById("cart-filled");
var emptyCart = document.getElementById('empty-cart');
var totalProducts = document.getElementById('cart-total-products');
var cartTotal = document.getElementById('cart-total');

var contador = 0; // Inicializamos el contador en 0

var totalP = counterValue.textContent = contador;
totalProducts.textContent = totalP;

counterBtn.addEventListener("click", function (event) {
  if (event.target.classList.contains("plus-icon")) {
      contador++;
  } else if (event.target.classList.contains("minus-icon")) {
      contador = Math.max(0, contador - 1);
  }
  counterValue.textContent = contador;
});

addToCartBtn.addEventListener("click", function () {
  // Actualizar el contenido y el estilo del elemento cart-amount
  if (contador === 0) {
      // Si el contador es 0, establece el display en 'none' para ambos elementos
      cartAmount.style.display = 'none';
      cartFilled.style.display = 'none';
      emptyCart.style.display = 'flex';
  } else {
      // Si el contador no es 0, actualiza el contenido y el estilo de cartAmount
      cartAmount.textContent = contador;
      emptyCart.style.display = 'none';
      cartFilled.style.display = 'flex';
  }

  // Actualizar totalProducts con el valor actualizado del contador
  totalProducts.textContent = contador;

  // Multiplicar el contador por 125 y asignar el resultado a cartTotal
  cartTotal.textContent = '$' + contador * 125;

  // Reiniciar el contador del botón
  contador = 0;
  counterValue.textContent = contador;
});

var deleteBtn = document.getElementById('delete-icon');

deleteBtn.addEventListener('click', function () {
  cartAmount.style.display = 'none';
  cartFilled.style.display = 'none';
  emptyCart.style.display = 'flex';
})

var cartModal = document.getElementById('cart-modal');
var shopCart = document.getElementById('shop-cart');

// Toggle para mostrar/ocultar el modal al hacer clic en el carrito
shopCart.addEventListener('click', function () {
  cartModal.classList.toggle('active');

  // Aplicar o quitar la clase al shop-cart cuando el modal está activo o inactivo
  if (cartModal.classList.contains('active')) {
      shopCart.classList.add('active');
  } else {
      shopCart.classList.remove('active');
  }
});

// Ocultar el modal al hacer clic fuera de él
window.addEventListener('click', function (event) {
  if (!cartModal.contains(event.target) && event.target !== shopCart) {
      cartModal.classList.remove('active');
      shopCart.classList.remove('active');
  }
});

// Get references to the counter buttons, value span, and add to cart button
const minusBtn = document.querySelector('.minus-btn');
const plusBtn = document.querySelector('.plus-btn');
const counterValue = document.querySelector('.counter-value');
const addToCartBtn = document.querySelector('.add-to-cart-btn');

// Initialize the counter variable (outside the functions for better practice)
let currentCount = 0;

// Function to decrease the counter value (but not below 0)
function decrementCounter() {
  if (currentCount > 0) {
    currentCount--;
    counterValue.textContent = currentCount;
  }
}

// Function to increment the counter value (optional limit can be added)
function incrementCounter() {
  currentCount++;
  counterValue.textContent = currentCount;
  // You can optionally add a limit check here:
  // if (currentCount >= desiredLimit) {
  //   currentCount = desiredLimit; // or disable the button
  // }
}

// Add event listeners to the buttons
minusBtn.addEventListener('click', decrementCounter);
plusBtn.addEventListener('click', incrementCounter);

// Add functionality to the "Add to Cart" button (example using alert)
addToCartBtn.addEventListener('click', () => {
  if (currentCount > 0) {
    alert(`You've added ${currentCount} item(s) to your cart!`);
  } else {
    alert('Please select a quantity before adding to cart.');
  }
});
