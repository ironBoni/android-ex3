using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using AspNetMvc.Data;
using AspNetMvc.Models;
using Models;

namespace AspNetMvc.Controllers {
    public class RatingsController : Controller {
        private readonly IDataService<Rating, int> service;

        public RatingsController()
        {
            service = new RatingService();
        }

        // GET: Ratings
        public IActionResult Index()
        {
            return View(service.GetAll());
        }

        public IActionResult Search()
        {
            return View("Index", service.GetAll());
        }
        [HttpPost]
        public IActionResult Search(string query)
        {

            if (query == null)
            {
                return View("Index",query);
            }
            else
            {
                var search = service.GetAll().FindAll(Rating => Rating.Feedback.Contains(query));
                return View("Index", search);
            }

        }
        public IActionResult SearchPart(string query)
        {

            if (query == null)
            {
                return PartialView(query);
            }
            else
            {
                var search = service.GetAll().FindAll(Rating => Rating.Feedback.Contains(query));
                if(search.Count == 0)
                {
                    return PartialView(null);
                }
                return PartialView(search);
            }

        }

        // GET: Ratings/Details/5
        public IActionResult Details(int id)
        {
            var rating = service.GetById(id);
            if (rating == null)
            {
                return NotFound();
            }

            return View(rating);
        }

        // GET: Ratings/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Ratings/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Create([Bind("ID,Rate,Name,Feedback")] Rating rating)
        {
            if (ModelState.IsValid)
            {
                service.Create(rating);
                return RedirectToAction(nameof(Index));
            }
            return View(rating);
        }

        // GET: Ratings/Edit/5
        public IActionResult Edit(int id)
        {
            var rating = service.GetById(id);
            if (rating == null)
            {
                return NotFound();
            }
            return View(rating);
        }

        // POST: Ratings/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Edit(int id, [Bind("ID,Rate,Name,Feedback")] Rating rating)
        {
            if (id != rating.ID)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    service.Update(rating);
                }
                catch (Exception)
                {
                    if (!RatingExists(rating.ID))
                    {
                        return NotFound();
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            return View(rating);
        }

        // GET: Ratings/Delete/5
        public IActionResult Delete(int id)
        {
            var rating = service.GetById(id);
            if (rating == null)
            {
                return NotFound();
            }

            return View(rating);
        }

        // POST: Ratings/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public IActionResult DeleteConfirmed(int id)
        {
            var rating = service.GetById(id);
            service.Delete(rating.ID);
            return RedirectToAction(nameof(Index));
        }

        private bool RatingExists(int id)
        {
            return service.GetAll().Any(rating => rating.ID == id);
        }
    }
}
