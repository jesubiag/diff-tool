package com.difftool

data class Album(val name: String, val year: Int, val songs: List<Song>, val genres: List<String>) {
    override fun toString(): String {
        return name
    }
}

data class Song(@AuditKey val songId: SongId, val name: String, val number: Int, val artist: Artist) {
    override fun toString(): String {
        return name
    }
}

data class SongId(val value: String) {
    override fun toString(): String {
        return value
    }
}

data class Artist(val name: String, val country: Country) {
    override fun toString(): String {
        return name
    }
}

data class Country(val name: String) {
    override fun toString(): String {
        return name
    }
}