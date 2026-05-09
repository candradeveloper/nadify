package com.nadify.innertube.pages

import com.nadify.innertube.models.Album
import com.nadify.innertube.models.AlbumItem
import com.nadify.innertube.models.Artist
import com.nadify.innertube.models.ArtistItem
import com.nadify.innertube.models.MusicResponsiveListItemRenderer
import com.nadify.innertube.models.MusicTwoRowItemRenderer
import com.nadify.innertube.models.PlaylistItem
import com.nadify.innertube.models.SongItem
import com.nadify.innertube.models.YTItem
import com.nadify.innertube.models.oddElements
import com.nadify.innertube.utils.parseTime

data class LibraryAlbumsPage(
    val albums: List<AlbumItem>,
    val continuation: String?,
) {
    companion object {
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
            return AlbumItem(
                        browseId = renderer.navigationEndpoint.browseEndpoint?.browseId ?: return null,
                        playlistId = renderer.thumbnailOverlay?.musicItemThumbnailOverlayRenderer?.content
                            ?.musicPlayButtonRenderer?.playNavigationEndpoint
                            ?.watchPlaylistEndpoint?.playlistId ?: return null,
                        title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                        artists = null,
                        year = renderer.subtitle?.runs?.lastOrNull()?.text?.toIntOrNull(),
                        thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl() ?: return null,
                        explicit = renderer.subtitleBadges?.find {
                            it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                        } != null
                    )
        }
    }
}
