"""Regenerate tier textures and JSON resources. Requires Pillow."""

from __future__ import annotations

import json
import os
from pathlib import Path
from PIL import Image

REPO = Path(__file__).resolve().parents[1]
AA_SRC = Path(r"C:\Users\Saereth\Documents\code\ActuallyAdditions\src\main\resources\assets\actuallyadditions\textures\block")

ASSETS = REPO / "src" / "main" / "resources" / "assets" / "aatiers"
DATA = REPO / "src" / "main" / "resources" / "data" / "aatiers"

TIERS = [
    # name, tier_color (RGB), tier_material item id, display_label
    ("iron",      (220, 220, 230), "minecraft:iron_ingot",       "Iron"),
    ("gold",      (255, 200,  80), "minecraft:gold_ingot",       "Gold"),
    ("diamond",   (130, 220, 230), "minecraft:diamond",          "Diamond"),
    ("netherite", ( 90,  70,  72), "minecraft:netherite_ingot",  "Netherite"),
]

TINT_STRENGTH = 0.45
ACCENT_STRENGTH = 0.75
ACCENT_LUMA_THRESHOLD = 130


def luma(px):
    r, g, b = px[0], px[1], px[2]
    return 0.299 * r + 0.587 * g + 0.114 * b


def tint(img: Image.Image, color, strength: float, accent_strength: float = 0.0) -> Image.Image:
    src = img.convert("RGBA")
    out = Image.new("RGBA", src.size)
    sp = src.load()
    op = out.load()
    cr, cg, cb = color
    for y in range(src.height):
        for x in range(src.width):
            r, g, b, a = sp[x, y]
            if a == 0:
                op[x, y] = (0, 0, 0, 0)
                continue
            s = strength
            if accent_strength > 0 and luma((r, g, b)) > ACCENT_LUMA_THRESHOLD:
                s = accent_strength
            nr = int(r * (1 - s) + cr * (r / 255.0) * s + cr * s * 0.25)
            ng = int(g * (1 - s) + cg * (g / 255.0) * s + cg * s * 0.25)
            nb = int(b * (1 - s) + cb * (b / 255.0) * s + cb * s * 0.25)
            op[x, y] = (max(0, min(255, nr)), max(0, min(255, ng)), max(0, min(255, nb)), a)
    return out


def write_json(path: Path, obj):
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2)


def generate_textures():
    top_src = Image.open(AA_SRC / "display_stand_top.png")
    side_src = Image.open(AA_SRC / "display_stand_side.png")
    base_src = Image.open(AA_SRC / "base_texture.png")
    out_dir = ASSETS / "textures" / "block"
    out_dir.mkdir(parents=True, exist_ok=True)

    for name, color, _, _ in TIERS:
        tint(top_src,  color, TINT_STRENGTH, ACCENT_STRENGTH).save(out_dir / f"{name}_display_stand_top.png")
        tint(side_src, color, TINT_STRENGTH).save(out_dir / f"{name}_display_stand_side.png")
        tint(base_src, color, TINT_STRENGTH).save(out_dir / f"{name}_base_texture.png")
        print(f"  textures: {name}")


def generate_resources():
    lang = {}
    lang["itemGroup.aatiers"] = "Actually Additions: Tiers"

    for name, _, material, label in TIERS:
        full_id = f"{name}_display_stand"
        ns_id = f"aatiers:{full_id}"

        write_json(ASSETS / "blockstates" / f"{full_id}.json", {
            "variants": {"": {"model": f"aatiers:block/{full_id}"}}
        })

        write_json(ASSETS / "models" / "block" / f"{full_id}.json", {
            "parent": "actuallyadditions:block/display_stand",
            "textures": {
                "base":     f"aatiers:block/{name}_base_texture",
                "side":     f"aatiers:block/{name}_display_stand_side",
                "particle": f"aatiers:block/{name}_display_stand_top",
                "top":      f"aatiers:block/{name}_display_stand_top"
            }
        })

        write_json(ASSETS / "models" / "item" / f"{full_id}.json", {
            "parent": f"aatiers:block/{full_id}"
        })

        write_json(DATA / "loot_table" / "blocks" / f"{full_id}.json", {
            "type": "minecraft:block",
            "pools": [{
                "bonus_rolls": 0.0,
                "conditions": [{"condition": "minecraft:survives_explosion"}],
                "entries": [{"type": "minecraft:item", "name": ns_id}],
                "functions": [{
                    "function": "minecraft:copy_components",
                    "include": ["actuallyadditions:energy"],
                    "source": "block_entity"
                }],
                "rolls": 1.0
            }],
            "random_sequence": f"aatiers:blocks/{full_id}"
        })

        prev_index = next(i for i, t in enumerate(TIERS) if t[0] == name) - 1
        prev_id = "actuallyadditions:display_stand" if prev_index < 0 else f"aatiers:{TIERS[prev_index][0]}_display_stand"
        write_json(DATA / "recipe" / f"{full_id}.json", {
            "type": "minecraft:crafting_shaped",
            "category": "misc",
            "key": {
                "M": {"item": material},
                "P": {"item": prev_id}
            },
            "pattern": ["MMM", "MPM", "MMM"],
            "result": {"count": 1, "id": ns_id}
        })

        lang[f"block.aatiers.{full_id}"] = f"{label} Display Stand"

    write_json(ASSETS / "lang" / "en_us.json", lang)


def main():
    print("Generating textures...")
    generate_textures()
    print("Generating JSON resources...")
    generate_resources()
    print("Done.")


if __name__ == "__main__":
    main()
