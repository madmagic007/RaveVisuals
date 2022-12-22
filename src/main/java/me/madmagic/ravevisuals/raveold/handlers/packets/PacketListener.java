package me.madmagic.ravevisuals.raveold.handlers.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.raveold.handlers.EditorHandler;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;

public class PacketListener {

    public static void init() {
        Main.pm.addPacketListener(new PacketAdapter(Main.instance, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                int entId = event.getPacket().getIntegers().read(0);

                FixtureHandler.activeFixtures.forEach((s, f) -> {
                    if (f.entityId() == entId) EditorHandler.editFixture(event.getPlayer(), f);
                });
            }
        });
    }
}
