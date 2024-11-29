function setupCarousel (elemId, onNavigate) {
    const rootEl = document.getElementById(elemId);
    const prevEl = rootEl.querySelector(".carousel__prev");
    const nextEl = rootEl.querySelector(".carousel__next");
    const items = rootEl.querySelectorAll(".carousel__item");
    const itemsCount = items.length;

    let activeIndex = 0;

    const prev = () => {
        if (activeIndex > 0) {
            items[activeIndex].classList.remove("carousel--active");

            activeIndex = activeIndex - 1;
            rootEl.style.setProperty("--carousel-index", activeIndex);
            items[activeIndex].classList.add("carousel--active");

            onNavigate(activeIndex);
        }
    };

    const next = () => {
        if (activeIndex < itemsCount - 1) {
            items[activeIndex].classList.remove("carousel--active");

            activeIndex = activeIndex + 1;
            rootEl.style.setProperty("--carousel-index", activeIndex);
            items[activeIndex].classList.add("carousel--active");

            onNavigate(activeIndex);
        }
    };

    prevEl.addEventListener("click", prev);
    nextEl.addEventListener("click", next);

    rootEl.addEventListener("keydown", (ev) => {
        switch (true) {
            case (ev.key === "ArrowLeft"):
                prev();
                break;

            case (ev.key === "ArrowRight"):
                next();
                break;

            default:
                break;
        }
    });
}

function setupDotsPagination (elemId) {
    const rootEl = document.getElementById(elemId);
    const dotsEl = rootEl.className === "dots" ? rootEl : rootEl.querySelector(".dots");
    const items = dotsEl.querySelectorAll(".dots__item");

    let currIndex = 0;
    let minIndex = 0;
    let maxIndex = 3;
    let offset = 0;

    const onNavigate = (ev) => {
        const newIndex = Array.from(items).findIndex((item) => item == ev.target);
        const isNext = newIndex > currIndex;

        dotsEl.querySelector(".dots--active")?.classList.remove("dots--active");
        dotsEl.querySelector(".dots--prev")?.classList.remove("dots--prev");
        dotsEl.querySelector(".dots--next")?.classList.remove("dots--next");

        items[newIndex].addEventListener("animationend", function () {
            dotsEl.querySelector(".dots--active")?.classList.remove("dots--active");

            this.classList.add("dots--active");
            this.classList.remove("dots--next");
            this.classList.remove("dots--prev");
            currIndex = newIndex;
        }, { once: true })

        if (isNext) {
            items[newIndex].classList.add("dots--next");
        } else {
            items[newIndex].classList.add("dots--prev");
        }

        if (isNext) {
            if (newIndex >= maxIndex) {
                offset = Math.min(Math.floor(items.length / 2), offset + 1);

                if (offset > minIndex) {
                    minIndex += 1;
                    maxIndex += 1;
                }
            }
        } else {
            if (newIndex <= minIndex) {
                offset = Math.max(0, offset - 1);

                if (offset < maxIndex) {
                    minIndex = Math.max(0, minIndex - 1);
                    maxIndex = Math.max(3, maxIndex - 1);
                }
            }
        }

        console.log("newIndex: %d minIndex: %d maxIndex: %d offset: %d", newIndex, minIndex, maxIndex, offset);

        dotsEl.style.setProperty("--dots-index", offset);

        items.forEach((item) => item.classList.remove("dots--edge"));

        if (minIndex > 0) {
            items[minIndex].classList.add("dots--edge");
        }

        if (maxIndex < items.length - 1) {
            items[maxIndex].classList.add("dots--edge");
        }
    }

    items.forEach((item) => item.addEventListener("navigate", onNavigate));

    if (minIndex > 0) {
        items[minIndex].classList.add("dots--edge");
    }

    if (maxIndex < items.length - 1) {
        items[maxIndex].classList.add("dots--edge");
    }

    const navigate = (index) => items[index].dispatchEvent(new Event("navigate"));

    return {
        onNavigate: navigate
    }
}

const { onNavigate } = setupDotsPagination("dots");

setupCarousel("carousel", onNavigate);

document.getElementById("carousel").focus();