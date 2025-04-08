package com.timeAuction.timeProduct.repository.userAuctionTicket;


import com.timeAuction.timeProduct.entity.user.User;
import com.timeAuction.timeProduct.entity.userAuctionTicket.UserAuctionTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuctionTicketRepository extends JpaRepository<UserAuctionTicket,Long> {

    Optional<UserAuctionTicket> findByUser(User user);
}
