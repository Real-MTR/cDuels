package xyz.atomdev.cduels.model.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

// I know I can use @Data
@AllArgsConstructor
@Getter
@Setter
public class Arena {

    private final String id;
    private String name;

    private Location pos1, pos2;
    private boolean occupied = false;

    public Arena(String id, String name) {
        this.id = id;
        this.name = name;
    }
}