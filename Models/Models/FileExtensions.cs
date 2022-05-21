using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public class FileExtensions {
        public static List<string> VideoExtensions = new List<string>()
        {
            "avi", "mp4", "mkv", "mpeg", "wmv",
            "mts", "ts", "tts", "wm", "aac", "adt",
            "adts", "asx", "dat", "ivf", "m1v", "m4a", "mod",
            "mod", "mp2", "mp3", "mpv2", "wmx", "mpv2",
            "m2t", "m2ts", "m4v", "mk3d", "mov", "mp2v", "mpa",
            "3g2", "3gp", "3gp2", "3gpp", "asf", "mp4", "mpg",
            "wmx", "wpl", "wvx"
        };

        public static List<string> AudioExtensions = new List<string>()
        {
            "mp3", "wav", "wma", "aif", "mka",
            "aifc", "asf", "au", "m3u", "mid", "rmi",
            "snd", "wax", "webp", "3gp", "aa", "aac",
            "aax", "act", "alac", "ogg",
            "m4a", "flac", "amr", "aac", "adts"
        };

        public static List<string> ImageExtensions = new List<string>()
        {
            "emf", "wmf", "jpg", "jpeg", "jfif", "webp",
            "jpe", "png", "bmp", "dib", "rle", "gif",
            "emz", "wmz", "tif", "tiff", "svg", "ico"
        };
    }
}
