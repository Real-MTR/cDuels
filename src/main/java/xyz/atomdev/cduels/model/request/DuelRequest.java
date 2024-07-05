package xyz.atomdev.cduels.model.request;

/*
 *
 * XeDuels is a property of Kira-Development-Team
 * 12/30/2023
 * Coded by the founders of Kira-Development-Team
 * EmpireMTR & Vifez
 *
 */

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.model.kit.Kit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class DuelRequest {

    @Getter private static Map<UUID, DuelRequest> requests = new HashMap<>();

    private final Player requester;
    private final Player target;

    private Kit kit;

    private long dateTime;
    private boolean expired;

    public DuelRequest(Player requester, Player target, Kit kit) {
        this.requester = requester;
        this.target = target;
        this.kit = kit;
        this.dateTime = System.currentTimeMillis();
        this.expired = false;

        requests.put(requester.getUniqueId(), this);
    }

    // After 30 seconds, it will expire
    public boolean isExpired() {
        if(!expired && ((System.currentTimeMillis() - dateTime) / 1000) >= 30) {
            this.expired = true;
            return expired;
        }

        this.expired = false;
        return expired;
    }

    public static DuelRequest getByRequester(UUID uuid) {
        return requests.get(uuid);
    }
}