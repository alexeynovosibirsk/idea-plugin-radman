package com.nazarov.radman.model;

import java.util.Objects;

public class CommunityRadioBrowser {

    private boolean url;
    private boolean url_resolved = true;
    private boolean name = true;
    private boolean homepage = true;
    private boolean countrycode = true;
    private boolean language = true;
    private boolean codec = true;
    private boolean bitrate = true;
    private boolean votes = true;

    public CommunityRadioBrowser() {
    }

    public boolean isUrl() {
        return url;
    }

    public void setUrl(boolean url) {
        this.url = url;
    }

    public boolean isUrl_resolved() {
        return url_resolved;
    }

    public void setUrl_resolved(boolean url_resolved) {
        this.url_resolved = url_resolved;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isHomepage() {
        return homepage;
    }

    public void setHomepage(boolean homepage) {
        this.homepage = homepage;
    }

    public boolean isCountrycode() {
        return countrycode;
    }

    public void setCountrycode(boolean countrycode) {
        this.countrycode = countrycode;
    }

    public boolean isLanguage() {
        return language;
    }

    public void setLanguage(boolean language) {
        this.language = language;
    }

    public boolean isCodec() {
        return codec;
    }

    public void setCodec(boolean codec) {
        this.codec = codec;
    }

    public boolean isBitrate() {
        return bitrate;
    }

    public void setBitrate(boolean bitrate) {
        this.bitrate = bitrate;
    }

    public boolean isVotes() {
        return votes;
    }

    public void setVotes(boolean votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CommunityRadioBrowser) obj;
        return
                this.url == that.url &&
                this.url_resolved == that.url_resolved &&
                this.name == that.name &&
                this.homepage == that.homepage &&
                this.countrycode == that.countrycode &&
                this.language == that.language &&
                this.codec == that.codec &&
                this.bitrate == that.bitrate &&
                this.votes == that.votes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, url_resolved, name, homepage, countrycode, language, codec, bitrate, votes);
    }

    @Override
    public String toString() {
        return "CommunityRadioBrowser[" +
                "url=" + url + ", " +
                "url_resolved=" + url_resolved + ", " +
                "name=" + name + ", " +
                "homepage=" + homepage + ", " +
                "countrycode=" + countrycode + ", " +
                "language=" + language + ", " +
                "codec=" + codec + ", " +
                "bitrate=" + bitrate + ", " +
                "votes=" + votes + ']';
    }


}
