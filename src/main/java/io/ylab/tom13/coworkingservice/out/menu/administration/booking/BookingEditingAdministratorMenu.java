package io.ylab.tom13.coworkingservice.out.menu.administration.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingEditMenu;

public class BookingEditingAdministratorMenu extends Menu {

    private final AdministrationClient administrationClient;

    private final BookingEditMenu bookingEditingMenu;

    public BookingEditingAdministratorMenu() {

        administrationClient = new AdministrationClient();
        bookingEditingMenu = new BookingEditMenu();
    }

    private UserDTO user;

    @Override
    public void display() {
        try {
            UserDTO admin = (UserDTO) localSession.getAttribute("user");
            user  = getUserToEditBooking(admin);
            localSession.setAttribute("user", user);
            bookingEditingMenu.display();
            localSession.setAttribute("user", admin);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }


    }

    private UserDTO getUserToEditBooking(UserDTO admin) throws UserNotFoundException {
        AuthenticationDTO authentication = new AuthenticationDTO(admin.id());
        String email = readString("Введите email пользователя: ");
        return administrationClient.getUserByEmail(authentication, email);
    }
}
