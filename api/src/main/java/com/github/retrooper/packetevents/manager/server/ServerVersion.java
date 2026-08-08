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

package com.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Server Version.
 * This is a nice wrapper over minecraft's protocol versions.
 * You won't have to memorize the protocol version, just memorize the server version you see in the launcher.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */

public enum ServerVersion {
    //TODO Rename to MinecraftVersion?
    V_1_7_2(4, 1), V_1_7_4(4, 2), V_1_7_5(4, 3),
    V_1_7_6(5, 4), V_1_7_7(5, 5), V_1_7_8(5, 6), V_1_7_9(5, 7), V_1_7_10(5, 8),
    V_1_8(47, 9), V_1_8_3(47, 10), V_1_8_8(47, 11),
    V_1_9(107, 12), V_1_9_1(108, 13), V_1_9_2(109, 14), V_1_9_4(110, 15),
    //1.10 and 1.10.1 are redundant
    V_1_10(210, 16), V_1_10_1(210, 17), V_1_10_2(210, 18),
    V_1_11(315, 19), V_1_11_2(316, 20),
    V_1_12(335, 21), V_1_12_1(338, 22), V_1_12_2(340, 23),
    V_1_13(393, 24), V_1_13_1(401, 25), V_1_13_2(404, 26),
    V_1_14(477, 27), V_1_14_1(480, 28), V_1_14_2(485, 29), V_1_14_3(490, 30), V_1_14_4(498, 31),
    V_1_15(573, 32), V_1_15_1(575, 33), V_1_15_2(578, 34),
    V_1_16(735, 35), V_1_16_1(736, 36), V_1_16_2(751, 37), V_1_16_3(753, 38), V_1_16_4(754, 39), V_1_16_5(754, 40),
    V_1_17(755, 41), V_1_17_1(756, 42),
    V_1_18(757, 43), V_1_18_1(757, 44), V_1_18_2(758, 45),
    //1.19.1 and 1.19.2 have the same protocol version
    V_1_19(759, 46), V_1_19_1(760, 47), V_1_19_2(760, 48), V_1_19_3(761, 49), V_1_19_4(762, 50),
    //1.20 and 1.20.1 have the same protocol version. 1.20.3 and 1.20.4 have the same protocol version. 1.20.5 and 1.20.6 have the same protocol version
    V_1_20(763, 51), V_1_20_1(763, 52), V_1_20_2(764, 53), V_1_20_3(765, 54), V_1_20_4(765, 55), V_1_20_5(766, 56), V_1_20_6(766, 57),
    //1.21 and 1.21.1 have the same protocol version. 1.21.2 and 1.21.3 have the same protocol version. 1.21.7 and 1.21.8 have the same protocol version. 1.21.9 and 1.21.10 have the same protocol version
    V_1_21(767, 58), V_1_21_1(767, 59), V_1_21_2(768, 60), V_1_21_3(768, 61), V_1_21_4(769, 62), V_1_21_5(770, 63), V_1_21_6(771, 64), V_1_21_7(772, 65), V_1_21_8(772, 66), V_1_21_9(773, 67), V_1_21_10(773, 68), V_1_21_11(774, 69),
    V_26_1(775, 70), V_26_1_1(775, 71), V_26_1_2(775, 72),
    V_26_2(776, 73),
    //TODO UPDATE Add server version constant
    ERROR(-1, true, 90);

    private static final ServerVersion[] VALUES = values();
    private static final ServerVersion[] REVERSED_VALUES;
    private static final Map<Integer, ServerVersion> BY_ID_MAP = new HashMap<>();

    static {
        REVERSED_VALUES = values();
        int i = 0;
        int j = REVERSED_VALUES.length - 1;
        ServerVersion tmp;
        while (j > i) {
            tmp = REVERSED_VALUES[j];
            REVERSED_VALUES[j--] = REVERSED_VALUES[i];
            REVERSED_VALUES[i++] = tmp;
        }
        for (ServerVersion version : VALUES) {
            BY_ID_MAP.putIfAbsent(version.protocolVersion, version);
        }
    }

    private final int protocolVersion;
    private final int releaseOrder;
    private final String name;
    private ClientVersion toClientVersion;

    ServerVersion(int protocolVersion,int releaseOrder) {
        this.protocolVersion = protocolVersion;
        this.releaseOrder = releaseOrder;
        this.name = name().substring(2).replace("_", ".");
    }

    ServerVersion(int protocolVersion, boolean isNotRelease, int releaseOrder) {
        this.protocolVersion = protocolVersion;
        this.releaseOrder = releaseOrder;
        if (isNotRelease) {
            this.name = name();
        } else {
            this.name = name().substring(2).replace("_", ".");
        }
    }

    public static ServerVersion[] reversedValues() {
        return REVERSED_VALUES;
    }

    public static ServerVersion getLatest() {
        return REVERSED_VALUES[1];
    }

    public static ServerVersion getOldest() {
        return VALUES[0];
    }

    //TODO Optimize
    /*@Deprecated
    public static ServerVersion getById(int protocolVersion) {
        for (ServerVersion version : VALUES) {
            if (version.protocolVersion == protocolVersion) {
                return version;
            }
        }
        return null;
    }*/
    @Deprecated
    public static ServerVersion getById(int protocolVersion) {
        return BY_ID_MAP.get(protocolVersion);
    }

    public ClientVersion toClientVersion() {
        ClientVersion local = this.toClientVersion;
        if (local == null) {
            local = ClientVersion.getById(this.protocolVersion);
            this.toClientVersion = local;
        }
        return local;
    }

    /**
     * Get the release name of this server version.
     * For example, for the V_1_18 enum constant, it would return "1.18".
     *
     * @return Release name
     */
    public String getReleaseName() {
        return name;
    }

    /**
     * Get this server version's protocol version.
     *
     * @return Protocol version.
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Is this server version newer than the compared server version?
     * This method simply checks if this server version's protocol version is greater than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than the compared server version.
     */
    /*public boolean isNewerThan(ServerVersion target) {
        return this.ordinal() > target.ordinal(); //为什么是用枚举？
    }*/
    public boolean isNewerThan(ServerVersion target) {
        return this.releaseOrder > target.releaseOrder;
    }



    /**
     * Is this server version older than the compared server version?
     * This method simply checks if this server version's protocol version is less than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than the compared server version.
     */
    /*public boolean isOlderThan(ServerVersion target) {
        return this.ordinal() < target.ordinal();
    }*/
    public boolean isOlderThan(ServerVersion target) {
        return this.releaseOrder < target.releaseOrder;
    }

    /**
     * Is this server version newer than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is greater than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than or equal to the compared server version.
     */
    public boolean isNewerThanOrEquals(ServerVersion target) {
        return this.releaseOrder >= target.releaseOrder;
    }

    /**
     * Is this server version older than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is older than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than or equal to the compared server version.
     */
    public boolean isOlderThanOrEquals(ServerVersion target) {
        return this.releaseOrder <= target.releaseOrder;
    }

    /**
     * Is this server version newer than, older than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is greater than, less than or equal to
     * the compared server version's protocol version.
     *
     * @param comparison    Comparison type.
     * @param targetVersion Compared server version.
     * @return true or false, based on the comparison type.
     * @see #isNewerThan(ServerVersion)
     * @see #isNewerThanOrEquals(ServerVersion)
     * @see #isOlderThan(ServerVersion)
     * @see #isOlderThanOrEquals(ServerVersion)
     */
    public boolean is(@NotNull VersionComparison comparison, @NotNull ServerVersion targetVersion) {
        switch (comparison) {
            case EQUALS:
                return this == targetVersion;
            case NEWER_THAN:
                return isNewerThan(targetVersion);
            case NEWER_THAN_OR_EQUALS:
                return isNewerThanOrEquals(targetVersion);
            case OLDER_THAN:
                return isOlderThan(targetVersion);
            case OLDER_THAN_OR_EQUALS:
                return isOlderThanOrEquals(targetVersion);
            default:
                return false;
        }
    }
}
