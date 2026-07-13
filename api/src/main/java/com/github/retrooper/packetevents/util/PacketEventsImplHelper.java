/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PacketEventsImplHelper {

    private PacketEventsImplHelper() {
    }

    public static @Nullable ProtocolPacketEvent handlePacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation, PacketSide side
    ) throws Exception {
        if (side == PacketSide.SERVER) {
            return handleClientBoundPacket(channel, user, player, buffer, autoProtocolTranslation);
        } else {
            return handleServerBoundPacket(channel, user, player, buffer, autoProtocolTranslation);
        }
    }

    public static @Nullable PacketSendEvent handleClientBoundPacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation
    ) throws Exception {
        if (!(buffer instanceof ByteBuf)) return null;
        ByteBuf buf = (ByteBuf) buffer;

        if (!buf.isReadable()) {
            return null;
        }

        int preProcessIndex = buf.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(channel, user, player, buffer, autoProtocolTranslation);
        int processIndex = buf.readerIndex();

        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            buf.readerIndex(processIndex);
        }, !autoProtocolTranslation);

        if (!packetSendEvent.isCancelled()) {
            //Did they ever use a wrapper?
            if (packetSendEvent.getLastUsedWrapper() != null) {
                //Rewrite the buffer
                buf.clear();
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            } else {
                //If no wrappers were used, just pass on the original buffer.
                //Correct the reader index, basically what the next handler is expecting.
                buf.readerIndex(preProcessIndex);
            }
        } else {
            //Make the buffer unreadable for the next handlers
            buf.clear();
        }

        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }

        return packetSendEvent;
    }

    public static @Nullable PacketReceiveEvent handleServerBoundPacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation
    ) throws Exception {
        if (!(buffer instanceof ByteBuf)) return null;
        ByteBuf buf = (ByteBuf) buffer;

        if (!buf.isReadable()) {
            return null;
        }

        int preProcessIndex = buf.readerIndex();

        PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
        int processIndex = buf.readerIndex();

        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
            buf.readerIndex(processIndex);
        }, !autoProtocolTranslation);


        if (!packetReceiveEvent.isCancelled()) {
            //Did they ever use a wrapper?
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
                //Rewrite the buffer
                buf.clear();
                packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                packetReceiveEvent.getLastUsedWrapper().write();
            } else {
                //If no wrappers were used, just pass on the original buffer.
                //Correct the reader index, basically what the next handler is expecting.
                buf.readerIndex(preProcessIndex);
            }
        } else {
            //Cancelling the packet, lets clear the buffer
            buf.clear();
        }
        if (packetReceiveEvent.hasPostTasks()) {
            for (Runnable task : packetReceiveEvent.getPostTasks()) {
                task.run();
            }
        }
        return packetReceiveEvent;
    }

    public static void handleDisconnection(Object channel, @Nullable UUID uuid) {
        synchronized (channel) {
            ProtocolManager protocolManager = PacketEvents.getAPI().getProtocolManager();
            User user = protocolManager.getUser(channel);

            if (user != null) {
                UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
                PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
                protocolManager.removeUser(user.getChannel());
            }

            if (uuid == null) {
                protocolManager.removeChannel(channel);
            } else {
                protocolManager.removeChannelById(uuid);
            }
        }
    }
}
