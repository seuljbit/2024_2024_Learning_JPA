import Cursor from './cursor.js';
import Grid from './grid.js';
import { preloadImages } from './utils.js';

gsap.to(".grid__item", { x: 100, duration: 1 });

// Preload  images
preloadImages('.grid__item-img, .bigimg').then(() => {
    // Remove loader (loading class)
    document.body.classList.remove('loading');
    
    // Initialize grid
    const grid = new Grid(document.querySelector('.grid'));
});

const cursor = new Cursor(document.querySelector('.cursor'));