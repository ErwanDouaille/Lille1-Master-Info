/* -*- mode: C; c-file-style: "gnu"; indent-tabs-mode: nil; -*- */

/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Douaille Erwan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Written by:
 *     Douaille Erwan <douailleerwan@gmail.com>
 */

#include "config.h"

#include <math.h>
#include <syslog.h>
#include <stdio.h>
#include <stdlib.h>

#include "shell-anamorphosis-effect.h"

struct _ShellAnamorphosisEffectPrivate
{
  float _x;
  float _y;
  float _z;

  int tex_width;
  int tex_height;
  CoglPipeline *pipeline;

  int tex_width_uniform;
  int tex_height_uniform;
  int _x_uniform;
  int _y_uniform;
  int _z_uniform;
};
typedef struct _ShellAnamorphosisEffectPrivate ShellAnamorphosisEffectPrivate;

G_DEFINE_TYPE_WITH_PRIVATE (ShellAnamorphosisEffect, shell_anamorphosis_effect, CLUTTER_TYPE_OFFSCREEN_EFFECT);

static const gchar *anamorphosis_decls =
  "uniform int tex_width;\n"
  "uniform int tex_height;\n"
  "uniform float _x;\n"
  "uniform float _y;\n"
  "uniform float _z;\n";
static const gchar *anamorphosis_pre =
"float width = (float(tex_width));\n"
"float height = (float(tex_height));\n"
"float x = clamp(_x, 0.0, 1.0);\n"
"float y = clamp(_y, 0.0, 1.0);\n"
"float z = clamp(_z, 0.0, 1.0);\n"
"float coeffX = 0.0;\n"
"float coeffY = 0.0;\n"
"float coeffZx = 8.0*width*(1-z);\n"
"float coeffZy = 8.0*height*(1-z);\n"
"float distanceX = width*abs(x- cogl_tex_coord.x);\n"
"float distanceY = height*abs(y- cogl_tex_coord.y);\n"

"coeffX = atan(distanceX,coeffZx) / atan((1.0),coeffZx);\n"
"coeffY = atan(distanceY,coeffZy) / atan((1.0),coeffZy);\n"
"if (x > cogl_tex_coord.x)\n"
  "cogl_tex_coord.x = x - coeffX/width;\n"
"else\n"
  "cogl_tex_coord.x = x + coeffX/width;\n"
"if (y > cogl_tex_coord.y)\n"
  "cogl_tex_coord.y = y - coeffY/height;\n"
"else\n"
  "cogl_tex_coord.y = y + coeffY/height;\n";

static gboolean
shell_anamorphosis_effect_pre_paint (ClutterEffect *effect)
{
  ShellAnamorphosisEffect *self = SHELL_ANAMORPHOSIS_EFFECT (effect);
  ShellAnamorphosisEffectPrivate *priv = shell_anamorphosis_effect_get_instance_private (self);

  if (!clutter_actor_meta_get_enabled (CLUTTER_ACTOR_META (effect)))
    return FALSE;

  /* If we're not doing any bending, we're not needed. */

  if (!clutter_feature_available (CLUTTER_FEATURE_SHADERS_GLSL))
    {
      /* if we don't have support for GLSL shaders then we
       * forcibly disable the ActorMeta
       */
      g_warning ("Unable to use the ShellAnamorphosisEffect: the "
                 "graphics hardware or the current GL driver does not "
                 "implement support for the GLSL shading language. The "
                 "effect will be disabled.");
      clutter_actor_meta_set_enabled (CLUTTER_ACTOR_META (effect), FALSE);
      return FALSE;
    }

  if (!CLUTTER_EFFECT_CLASS (shell_anamorphosis_effect_parent_class)->pre_paint (effect))
    return FALSE;

  ClutterOffscreenEffect *offscreen_effect = CLUTTER_OFFSCREEN_EFFECT (effect);
  CoglObject *texture;

  texture = clutter_offscreen_effect_get_texture (offscreen_effect);
  cogl_pipeline_set_layer_texture (priv->pipeline, 0, texture);

  priv->tex_width = cogl_texture_get_width (texture);
  priv->tex_height = cogl_texture_get_height (texture);

  cogl_pipeline_set_uniform_1i (priv->pipeline, priv->tex_width_uniform, priv->tex_width);
  cogl_pipeline_set_uniform_1i (priv->pipeline, priv->tex_height_uniform, priv->tex_height);

  return TRUE;
}

static void
shell_anamorphosis_effect_paint_target (ClutterOffscreenEffect *effect)
{
  ShellAnamorphosisEffect *self = SHELL_ANAMORPHOSIS_EFFECT (effect);
  ShellAnamorphosisEffectPrivate *priv = shell_anamorphosis_effect_get_instance_private (self);
  CoglFramebuffer *fb = cogl_get_draw_framebuffer ();
  ClutterActor *actor;
  guint8 paint_opacity;

  actor = clutter_actor_meta_get_actor (CLUTTER_ACTOR_META (effect));
  paint_opacity = clutter_actor_get_paint_opacity (actor);


  cogl_pipeline_set_color4ub (priv->pipeline,
                              paint_opacity,
                              paint_opacity,
                              paint_opacity,
                              paint_opacity);

  cogl_framebuffer_draw_rectangle (fb, priv->pipeline,
                                   0, 0, priv->tex_width, priv->tex_height);
}

static void
shell_anamorphosis_effect_class_init (ShellAnamorphosisEffectClass *klass)
{
  ClutterEffectClass *effect_class = CLUTTER_EFFECT_CLASS (klass);
  ClutterOffscreenEffectClass *offscreen_class = CLUTTER_OFFSCREEN_EFFECT_CLASS (klass);

  offscreen_class->paint_target = shell_anamorphosis_effect_paint_target;
  effect_class->pre_paint = shell_anamorphosis_effect_pre_paint;
}

static void
update_uniforms (ShellAnamorphosisEffect *self)
{
  ShellAnamorphosisEffectPrivate *priv = shell_anamorphosis_effect_get_instance_private (self);
  cogl_pipeline_set_uniform_1f (priv->pipeline, priv->_x_uniform, priv->_x);
  cogl_pipeline_set_uniform_1f (priv->pipeline, priv->_y_uniform, priv->_y);
  cogl_pipeline_set_uniform_1f (priv->pipeline, priv->_z_uniform, priv->_z);
}

static void
shell_anamorphosis_effect_init (ShellAnamorphosisEffect *self)
{
  static CoglPipeline *pipeline_template;

  ShellAnamorphosisEffectPrivate *priv = shell_anamorphosis_effect_get_instance_private (self);

  if (G_UNLIKELY (pipeline_template == NULL))
    {
      CoglSnippet *snippet;
      CoglContext *ctx = clutter_backend_get_cogl_context (clutter_get_default_backend ());

      pipeline_template = cogl_pipeline_new (ctx);

      snippet = cogl_snippet_new (COGL_SNIPPET_HOOK_TEXTURE_LOOKUP, anamorphosis_decls, NULL);
      cogl_snippet_set_pre (snippet, anamorphosis_pre);
      cogl_pipeline_add_layer_snippet (pipeline_template, 0, snippet);
      cogl_object_unref (snippet);

      cogl_pipeline_set_layer_null_texture (pipeline_template,
                                            0, /* layer number */
                                            COGL_TEXTURE_TYPE_2D);
    }

  priv->pipeline = cogl_pipeline_copy (pipeline_template);
  priv->tex_width_uniform = cogl_pipeline_get_uniform_location (priv->pipeline, "tex_width");
  priv->tex_height_uniform = cogl_pipeline_get_uniform_location (priv->pipeline, "tex_height");
  priv->_x_uniform = cogl_pipeline_get_uniform_location (priv->pipeline, "_x");
  priv->_y_uniform = cogl_pipeline_get_uniform_location (priv->pipeline, "_y");
  priv->_z_uniform = cogl_pipeline_get_uniform_location (priv->pipeline, "_z");

  update_uniforms (self);
}

void
shell_anamorphosis_effect_update (ShellAnamorphosisEffect *self, float x, float y, float z)

{
  ShellAnamorphosisEffectPrivate *priv = shell_anamorphosis_effect_get_instance_private (self);
  priv->_x = x;
  priv->_y = y;
  priv->_z = z;
  
  update_uniforms (self);
  clutter_effect_queue_repaint (CLUTTER_EFFECT (self));
}
