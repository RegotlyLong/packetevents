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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client Version.
 * This is a nice tool for minecraft's client protocol versions.
 * You won't have to memorize the protocol version, just memorize the client version
 * as the version you see in the minecraft launcher.
 * Some enum constants may represent two or more versions as there have been cases where some versions have the same protocol version due to no protocol changes.
 * We added a comment over those enum constants so check it out.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */
public enum ClientVersion {
    V_1_7_2(4, 1),
    V_1_7_10(5, 2),

    V_1_8(47, 3),

    V_1_9(107, 4), V_1_9_1(108, 5), V_1_9_2(109, 6),
    /**
     * 1.9.3 or 1.9.4 as they have the same protocol version.
     */
    V_1_9_3(110, 7),
    V_1_10(210, 8),
    V_1_11(315, 9),
    /**
     * 1.11.1 or 1.11.2 as they have the same protocol version.
     */
    V_1_11_1(316, 10),
    V_1_12(335, 11), V_1_12_1(338, 12), V_1_12_2(340, 13),

    V_1_13(393, 14), V_1_13_1(401, 15), V_1_13_2(404, 16),

    V_1_14(477, 17), V_1_14_1(480, 18), V_1_14_2(485, 19),
    V_1_14_3(490, 20), V_1_14_4(498, 21),

    V_1_15(573, 22), V_1_15_1(575, 23), V_1_15_2(578, 24),

    V_1_16(735, 25), V_1_16_1(736, 26), V_1_16_2(751, 27),
    V_1_16_3(753, 28),
    /**
     * 1.16.4 or 1.16.5 as they have the same protocol version.
     */
    V_1_16_4(754, 29),

    V_1_17(755, 30), V_1_17_1(756, 31),

    /**
     * 1.18 or 1.18.1 as they have the same protocol version.
     */
    V_1_18(757, 32),
    V_1_18_2(758, 33),

    V_1_19(759, 34),
    /**
     * 1.19.1 and 1.19.2 have the same protocol version.
     */
    V_1_19_1(760, 35),
    V_1_19_3(761, 36),
    V_1_19_4(762, 37),
    /**
     * 1.20 and 1.20.1 have the same protocol version.
     */
    V_1_20(763, 38),
    V_1_20_2(764, 39),
    /**
     * 1.20.3 and 1.20.4 have the same protocol version.
     */
    V_1_20_3(765, 40),
    /**
     * 1.20.5 and 1.20.6 have the same protocol version.
     */
    V_1_20_5(766, 41),

    /**
     * 1.21 and 1.21.1 have the same protocol version.
     */
    V_1_21(767, 42),
    /**
     * 1.21.2 and 1.21.3 have the same protocol version.
     */
    V_1_21_2(768, 43),
    V_1_21_4(769, 44),
    V_1_21_5(770, 45),
    V_1_21_6(771, 46),
    /**
     * 1.21.7 and 1.21.8 have the same protocol version.
     */
    V_1_21_7(772, 47),
    /**
     * 1.21.9 and 1.21.10 have the same protocol version.
     */
    V_1_21_9(773, 48),
    V_1_21_11(774, 49),

    V_26_1(775, 50),
    V_26_2(776, 51),
    //TODO UPDATE Add new protocol version field

    @Deprecated
    LOWER_THAN_SUPPORTED_VERSIONS(V_1_7_2.protocolVersion - 1, true, 0), // 排序在支持的最老版本(1)之前
    //TODO UPDATE Update HIGHER_THAN_SUPPORTED_VERSIONS field
    @Deprecated
    HIGHER_THAN_SUPPORTED_VERSIONS(V_26_2.protocolVersion + 1, true, 1000), // 赋予极大的排序值，以保证"比所有最新版都新"

    UNKNOWN(-1, true, 1001);

    private static final ClientVersion[] VALUES = values();
    private static final ClientVersion[] REVERSED_VALUES;
    private static final Map<Integer, ClientVersion> BY_ID_MAP = new HashMap<>();

    static {
        List<ClientVersion> valuesAsList = Arrays.asList(values());
        Collections.reverse(valuesAsList);
        REVERSED_VALUES = valuesAsList.toArray(new ClientVersion[0]);

        for (ClientVersion version : VALUES) {
            BY_ID_MAP.putIfAbsent(version.protocolVersion, version);
        }
    }

    private static final int LOWEST_SUPPORTED_PROTOCOL_VERSION = LOWER_THAN_SUPPORTED_VERSIONS.protocolVersion + 1;
    private static final int HIGHEST_SUPPORTED_PROTOCOL_VERSION = HIGHER_THAN_SUPPORTED_VERSIONS.protocolVersion - 1;

    private final int protocolVersion;
    private final int releaseOrder;
    private final String name;
    private ServerVersion serverVersion;

    ClientVersion(int protocolVersion, int releaseOrder) {
        this.protocolVersion = protocolVersion;
        this.releaseOrder = releaseOrder;
        this.name = name().substring(2).replace("_", ".");
    }

    ClientVersion(int protocolVersion, boolean isNotRelease, int releaseOrder) {
        this.protocolVersion = protocolVersion;
        this.releaseOrder = releaseOrder;
        if (isNotRelease) {
            this.name = name();
        } else {
            this.name = name().substring(2).replace("_", ".");
        }
    }

    public static boolean isPreRelease(int protocolVersion) {
        return getLatest().protocolVersion <= protocolVersion
                || getOldest().protocolVersion >= protocolVersion;
    }

    public static boolean isRelease(int protocolVersion) {
        return protocolVersion <= getLatest().protocolVersion
                && protocolVersion >= getOldest().protocolVersion;
    }

    public boolean isPreRelease() {
        return isPreRelease(protocolVersion);
    }

    public boolean isRelease() {
        return isRelease(protocolVersion);
    }

    /**
     * Get the release name of this client version.
     * For example, for the V_1_18 enum constant, it would return "1.18".
     *
     * @return Release name
     */
    public String getReleaseName() {
        return name;
    }

    /**
     * Get a ClientVersion enum by protocol version.
     *
     * @param protocolVersion Protocol version.
     * @return ClientVersion
     */
    @NotNull
    public static ClientVersion getById(int protocolVersion) {
        if (protocolVersion < LOWEST_SUPPORTED_PROTOCOL_VERSION) {
            return getOldest();
        } else if (protocolVersion > HIGHEST_SUPPORTED_PROTOCOL_VERSION) {
            return getLatest();
        } else {
            ClientVersion version = BY_ID_MAP.get(protocolVersion);
            return version != null ? version : UNKNOWN;
        }
    }

    public static ClientVersion getLatest() {
        return REVERSED_VALUES[3];
    }

    public static ClientVersion getOldest() {
        return VALUES[0];
    }

    @Deprecated
    public ServerVersion toServerVersion() {
        ServerVersion local = this.serverVersion;
        if (local == null) {
            local = ServerVersion.getById(this.protocolVersion);
            this.serverVersion = local;
        }
        return local;
    }

    /**
     * Protocol version of this client version.
     *
     * @return Protocol version.
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    public int getReleaseOrder() {
        return releaseOrder;
    }

    /**
     * Is this client version newer than the compared client version?
     * This method simply checks if this client version's release order is greater than
     * the compared client version's release order.
     *
     * @param target Compared client version.
     * @return Is this client version newer than the compared client version.
     */
    public boolean isNewerThan(ClientVersion target) {
        return this.releaseOrder > target.releaseOrder;
    }

    /**
     * Is this client version newer than or equal to the compared client version?
     * This method simply checks if this client version's release order is newer than or equal to
     * the compared client version's release order.
     *
     * @param target Compared client version.
     * @return Is this client version newer than or equal to the compared client version.
     */
    public boolean isNewerThanOrEquals(ClientVersion target) {
        return this.releaseOrder >= target.releaseOrder;
    }

    /**
     * Is this client version older than the compared client version?
     * This method simply checks if this client version's release order is less than
     * the compared client version's release order.
     *
     * @param target Compared client version.
     * @return Is this client version older than the compared client version.
     */
    public boolean isOlderThan(ClientVersion target) {
        return this.releaseOrder < target.releaseOrder;
    }

    /**
     * Is this client version older than or equal to the compared client version?
     * This method simply checks if this client version's release order is older than or equal to
     * the compared client version's release order.
     *
     * @param target Compared client version.
     * @return Is this client version older than or equal to the compared client version.
     */
    public boolean isOlderThanOrEquals(ClientVersion target) {
        return this.releaseOrder <= target.releaseOrder;
    }

    /**
     * Is this client version newer than, older than or equal to the compared client version?
     * This method simply checks if this client version's release order is greater than, less than or equal to
     * the compared client version's release order.
     *
     * @param comparison    Comparison type.
     * @param targetVersion Compared client version.
     * @return true or false, based on the comparison type.
     * @see #isNewerThan(ClientVersion)
     * @see #isNewerThanOrEquals(ClientVersion)
     * @see #isOlderThan(ClientVersion)
     * @see #isOlderThanOrEquals(ClientVersion)
     */
    public boolean is(@NotNull VersionComparison comparison, @NotNull ClientVersion targetVersion) {
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