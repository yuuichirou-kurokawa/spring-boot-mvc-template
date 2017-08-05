package info.ewai.sbmt.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import info.ewai.sbmt.domain.Book;
import info.ewai.sbmt.service.BookService;
import info.ewai.sbmt.web.form.BookForm;

@Controller
public class BookController {

    @Autowired
    BookService bookservice;

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String index(Model model) {
        List<Book> list = this.bookservice.findAll();
        model.addAttribute("booklist", list);
        model.addAttribute("bookForm", new BookForm());
        return "book";
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public String search(@ModelAttribute BookForm bookForm, BindingResult result, Model model) {
        List<Book> list = this.bookservice.findByBookNameLikeAndTagLike(bookForm.getBookName(), bookForm.getTag());
        model.addAttribute("booklist", list);
        return "book";
    }

    @RequestMapping(value = "/book/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "/book-edit";
    }

    @RequestMapping(value = "/book/edit/{bookId}", method = RequestMethod.GET)
    public String edit(@PathVariable Long bookId, Model model) {
        if (StringUtils.isEmpty(bookId)) {
            model.addAttribute("bookForm", new BookForm());
            return "/book-edit";
        }
        Book book = this.bookservice.findOne((bookId));
        if (book == null) {
            // error, not found data
            return "book-edit";
        } else {
            model.addAttribute("bookForm", new BookForm(book));
        }
        return "book-edit";
    }

    @RequestMapping(value = "/book/save", method = RequestMethod.POST)
    public String save(@ModelAttribute BookForm bookForm, BindingResult result, Model model) {
        this.bookservice.save(new Book(bookForm));
        return "book-complete";
    }

    @RequestMapping(value = "/book/delete/{bookId}", method = RequestMethod.GET)
    public String delete(@PathVariable Long bookId, Model model) {
        if (StringUtils.isEmpty(bookId)) {
            // error
            return "book-edit";
        }
        Book book = this.bookservice.findOne((bookId));
        if (book == null) {
            // error, not found data
            return "book-edit";
        } else {
            this.bookservice.delete(book);
        }
        return "book-complete";
    }

}